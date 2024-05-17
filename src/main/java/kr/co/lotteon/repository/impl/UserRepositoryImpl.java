package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.entity.QUser;
import kr.co.lotteon.entity.QUserPoint;
import kr.co.lotteon.entity.Seller;
import kr.co.lotteon.entity.User;
import kr.co.lotteon.repository.custom.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private QUser qUser = QUser.user;
    private QUserPoint qUserPoint = QUserPoint.userPoint;

    // 마이페이지 출력을 위해 user_id로 유저 정보 조회
    public User selectUserInfo(String userId) {
        return jpaQueryFactory
                .selectFrom(qUser)
                .where(qUser.userId.eq(userId))
                .fetchOne();
    }
    //public String

    // UserId 찾기
    public Optional<User> findUserIdByUserNameAndUserEmail(String userName, String userEmail) {
        User user = jpaQueryFactory
                .selectFrom(qUser)
                .where(qUser.userName.eq(userName)
                        .and(qUser.userEmail.eq(userEmail)))
                .fetchOne();

        return Optional.ofNullable(user);
    }

    // UserPw update
    @Transactional
    public long updateUserPwByUserIdAndUserEmail(String userId, String userPw, String userEmail) {
        try {
            long result = jpaQueryFactory
                    .update(qUser)
                    .set(qUser.userPw, userPw)
                    .where(qUser.userId.eq(userId)
                            .and(qUser.userEmail.eq(userEmail)))
                    .execute();
            log.info("impl : " + result);
            return result;
        } catch (Exception e) {
            log.error("Error msg :" + e.getMessage());
            return -1;
        }
    }

    public Tuple selectUserInfoWithPoint(String userId){
        return jpaQueryFactory
                .select(qUser, qUserPoint)
                .from(qUser)
                .join(qUserPoint)
                .on(qUser.userId.eq(qUserPoint.userId))
                .where(qUser.userId.eq(userId))
                .fetchOne();
    }

    // 관리자 - 회원관리 - 회원 현황 조회
    public Page<User> selectSellerList(Pageable pageable, PageRequestDTO pageRequestDTO){

// 검색 여부에 따라 Seller 조회
        BooleanExpression expression = null;
        QueryResults<User> resultUser = null;
        if (pageRequestDTO.getKeyword() != null) {
            if (pageRequestDTO.getType().equals("userName")){
                expression = qUser.userName.contains(pageRequestDTO.getKeyword());
            }else if (pageRequestDTO.getType().equals("userRole")){
                expression = qUser.userRole.contains(pageRequestDTO.getKeyword());
            }else if (pageRequestDTO.getType().equals("userGrade")){
                expression = qUser.userGrade.contains(pageRequestDTO.getKeyword());
            }else if (pageRequestDTO.getType().equals("userStatus")){
                expression = qUser.userStatus.contains(pageRequestDTO.getKeyword());
            }
            resultUser = jpaQueryFactory
                    .selectFrom(qUser)
                    .where(expression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }else {
            resultUser = jpaQueryFactory
                    .selectFrom(qUser)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }
        List<User> userList = resultUser.getResults();
        long total = resultUser.getTotal();
        return new PageImpl<>(userList, pageable, total);
    }
}