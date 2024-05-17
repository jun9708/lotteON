package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.dto.PointHistoryDTO;
import kr.co.lotteon.dto.UserPointDTO;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.custom.UserPointRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUserPoint qUserPoint = QUserPoint.userPoint;
    private final QPointHistory qPointHistory = QPointHistory.pointHistory;
    private final QUser qUser = QUser.user;
    private final ModelMapper modelMapper;
    
    //My/point 내역 조회
    public Page<PointHistory> selectPoints(String userId, PageRequestDTO pageRequestDTO, Pageable pageable) {
        // userPoint 테이블에서 pointNO 구하고, pointHistory 조회
        log.info("IMPL 시작");

        // 0. 카테고리에 따른 where절 작성
        // 1. orders테이블에서 10개 조회
        // SELECT orderNo FROM orders WHERE userId = '?' ORDER BY orderDate DESC LIMIT 10
        long total = 0;

        BooleanExpression expression = null; //where절 보관용
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);//일주일전
        LocalDate fifteenDays = LocalDate.now().minusDays(15);//15일전
        LocalDate oneMonthsAgo = LocalDate.now().minusMonths(1);//한달전
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);//3개월전
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);//6개월전
        LocalDate yearOneAgo = LocalDate.now().minusYears(1);//1년전

        log.info("fifteenDays : " +fifteenDays);
        log.info("오늘 날짜 :" +LocalDate.now().plusDays(1).atStartOfDay());

        if (pageRequestDTO.getCate() != null) {
            // 날짜로 검색하는 경우
            if (pageRequestDTO.getCate().equals("week")) {
                expression = qPointHistory.changeDate.between(oneWeekAgo.atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
            } else if (pageRequestDTO.getCate().equals("15day")) {
                expression = qPointHistory.changeDate.between(fifteenDays.atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
            } else if (pageRequestDTO.getCate().equals("month")) {
                expression = qPointHistory.changeDate.between(oneMonthsAgo.atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
            } else if (pageRequestDTO.getCate().equals("3month")) {
                expression = qPointHistory.changeDate.between(threeMonthsAgo.atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
            } else if (pageRequestDTO.getCate().equals("6month")) {
                expression = qPointHistory.changeDate.between(sixMonthsAgo.atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
            } else if (pageRequestDTO.getCate().equals("year")) {
                expression = qPointHistory.changeDate.between(yearOneAgo.atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
            } else if (pageRequestDTO.getCate().equals("custom")) {
                // 시작날짜
                // 마지막날짜

                LocalDate startDate = pageRequestDTO.getStartDate();
                LocalDate finalDate = pageRequestDTO.getFinalDate();

                log.info("시작날짜 : " +pageRequestDTO.getStartDate());
                log.info("마지막날짜 : " +pageRequestDTO.getFinalDate());

                expression = qPointHistory.changeDate.between(startDate.atStartOfDay(), finalDate.atStartOfDay().plusDays(1).minusSeconds(1));
                log.info("계산된 날짜 : " +expression);
            }
            QueryResults<PointHistory> selectPointNo = jpaQueryFactory
                    .select(qPointHistory)
                    .from(qUserPoint)
                    .join(qPointHistory)
                    .on(qUserPoint.pointNo.eq(qPointHistory.pointNo))
                    .where(qUserPoint.userId.eq(userId))
                    .where(expression)
                    .orderBy(qPointHistory.changeDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            total = jpaQueryFactory
                    .select(qPointHistory.count())
                    .from(qUserPoint)
                    .join(qPointHistory)
                    .on(qUserPoint.pointNo.eq(qPointHistory.pointNo))
                    .where(qUserPoint.userId.eq(userId))
                    .where(expression)
                    .orderBy(qPointHistory.changeDate.desc())
                    .fetchOne();

            List<PointHistory> pointHistoryResult = selectPointNo.getResults();
            total = selectPointNo.getTotal();
            log.info("total : " + total);

            return new PageImpl<>(pointHistoryResult, pageable, total);

        } else {
            // 날짜로 검색이 아닌경우
            QueryResults<PointHistory> selectPointNo = jpaQueryFactory
                    .select(qPointHistory)
                    .from(qUserPoint)
                    .join(qPointHistory)
                    .on(qUserPoint.pointNo.eq(qPointHistory.pointNo))
                    .where(qUserPoint.userId.eq(userId))
                    .orderBy(qPointHistory.changeDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            total = jpaQueryFactory
                    .select(qPointHistory.count())
                    .from(qUserPoint)
                    .join(qPointHistory)
                    .on(qUserPoint.pointNo.eq(qPointHistory.pointNo))
                    .where(qUserPoint.userId.eq(userId))
                    .orderBy(qPointHistory.changeDate.desc())
                    .fetchOne();

            List<PointHistory> pointHistoryResult = selectPointNo.getResults();
            total = selectPointNo.getTotal();


            return new PageImpl<>(pointHistoryResult, pageable, total);
        }
    }

    @Override
    public List<PointHistory> myHomeSelectPoints(String userId) {

        //Point조회
    QueryResults<PointHistory> myPointHistory = jpaQueryFactory
                                                .select(qPointHistory)
                                                .from(qUserPoint)
                                                .join(qPointHistory)
                                                .on(qUserPoint.pointNo.eq(qPointHistory.pointNo))
                                                .where(qUserPoint.userId.eq(userId))
                                                .orderBy(qPointHistory.changeDate.desc())
                                                .limit(5)
                                                .fetchResults();

    List<PointHistory> myPointHistoryResult = myPointHistory.getResults();

    log.info("myPointHistoryResult" +myPointHistoryResult);
    return myPointHistoryResult;
    }

    // 관리자 - 회원관리 - 포인트관리 (ㅎ) //
    public PageResponseDTO userPointControl(PageRequestDTO pageRequestDTO, Pageable pageable) {

        QueryResults<Tuple> pointResult = jpaQueryFactory
                .select(qUserPoint, qUser.userName)
                .from(qUserPoint)
                .join(qUser)
                .on(qUserPoint.userId.eq(qUser.userId))
                .orderBy(qUserPoint.pointNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> userPointList = pointResult.getResults();
        int total = (int) pointResult.getTotal();
        Page<Tuple> userPointPage = new PageImpl<>(userPointList, pageable, total);

        List<UserPointDTO> userPointDTOList = userPointPage.getContent().stream()
                .map(tuple -> {
                    UserPoint userPoint = tuple.get(0, UserPoint.class);
                    String userName = tuple.get(1, String.class);
                    UserPointDTO userPointDTO = modelMapper.map(userPoint, UserPointDTO.class);
                    userPointDTO.setUserName(userName);
                    return userPointDTO;
                }).toList();

        List<UserPointDTO> resultPointDTO = new ArrayList<>();
        for (UserPointDTO eachPoint : userPointDTOList) {
            List<PointHistory> pointHistoryList = jpaQueryFactory
                    .selectFrom(qPointHistory)
                    .where(qPointHistory.pointNo.eq(eachPoint.getPointNo()))
                    .orderBy(qPointHistory.pointHisNo.desc())
                    .limit(5)
                    .fetch();

            List<PointHistoryDTO> pointHistoryDTO = new ArrayList<>();
            for (PointHistory eachHistory : pointHistoryList) {
                PointHistoryDTO historyDTO = modelMapper.map(eachHistory, PointHistoryDTO.class);
                pointHistoryDTO.add(historyDTO);
            }
            eachPoint.setHistoryDTOList(pointHistoryDTO);
            resultPointDTO.add(eachPoint);
        }

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(resultPointDTO)
                .total((int)userPointPage.getTotalElements())
                .build();
    }
}
