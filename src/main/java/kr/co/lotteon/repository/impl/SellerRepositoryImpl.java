package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.custom.SellerRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class SellerRepositoryImpl implements SellerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;
    private final QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final QProduct qProduct = QProduct.product;
    private final QProductimg qProductimg = QProductimg.productimg;
    private final QOrders qOrders = QOrders.orders;

    private final QSeller qSeller = QSeller.seller;

    // 판매자 메인페이지 - 홈 출력 정보 조회 (기간별 주문 건수 & 주문 금액 & 배송 현황 집계) //
    @Override
    public SellerInfoDTO selectSellerInfo(String prodSeller){

        // 필요한 정보를 한번에 처리할 DTO 생성
        SellerInfoDTO sellerInfoDTO = new SellerInfoDTO();

        // 신규 주문 건수 & 신규 주문 금액 (하루)
        LocalDate oneDay = LocalDate.now().minusDays(1);
        Tuple oneDayInfo = jpaQueryFactory
                            .select(qOrderDetail.count(), qOrderDetail.detailPrice.sum())
                            .from(qOrderDetail)
                            .where(qOrderDetail.prodSeller.eq(prodSeller))
                            .where(qOrderDetail.detailDate.between(oneDay, LocalDate.now()))
                            .fetchOne();

        // 주간 주문 건수 & 주간 주문 금액 (최근 일주일)
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        Tuple oneWeekInfo = jpaQueryFactory
                .select(qOrderDetail.count(), qOrderDetail.detailPrice.sum())
                .from(qOrderDetail)
                .where(qOrderDetail.prodSeller.eq(prodSeller))
                .where(qOrderDetail.detailDate.between(oneWeekAgo, LocalDate.now()))
                .fetchOne();

        // 월간 주문 건수 & 월간 주문 금액 (최근 한달)
        LocalDate oneMonthsAgo = LocalDate.now().minusMonths(1);
        Tuple oneMonthsInfo = jpaQueryFactory
                .select(qOrderDetail.count(), qOrderDetail.detailPrice.sum())
                .from(qOrderDetail)
                .where(qOrderDetail.prodSeller.eq(prodSeller))
                .where(qOrderDetail.detailDate.between(oneMonthsAgo, LocalDate.now()))
                .fetchOne();

        // 전체 주문 건수 & 전체 주문 금액
        Tuple allInfo = jpaQueryFactory
                .select(qOrderDetail.count(), qOrderDetail.detailPrice.sum())
                .from(qOrderDetail)
                .where(qOrderDetail.prodSeller.eq(prodSeller))
                .fetchOne();

        // 각 status별 개수 조회
        List<Tuple> allStatus = jpaQueryFactory
                .select(qOrderDetail.detailStatus, qOrderDetail.count())
                .from(qOrderDetail)
                .where(qOrderDetail.prodSeller.eq(prodSeller))
                .groupBy(qOrderDetail.detailStatus)
                .fetch();

        // 각 status별 개수를 Key-Value값으로 저장
        Map<String, Long> statusCountMap = new HashMap<>();
        for (Tuple tuple : allStatus) {
            String status = tuple.get(0, String.class);
            Long count = tuple.get(1, Long.class);
            statusCountMap.put(status, count);
        }

        // 지금까지 조회한 정보를 하나의 DTO에 넣어서 반환
        if (oneDayInfo.get(0, Long.class) != null && oneDayInfo.get(0, Long.class) != 0) {
            sellerInfoDTO.setOneDayCount(oneDayInfo.get(0, Long.class));
            sellerInfoDTO.setOneDayPrice(oneDayInfo.get(1, Integer.class));
        }else {
            sellerInfoDTO.setOneDayCount(0L);
            sellerInfoDTO.setOneDayPrice(0);
        }

        if (oneWeekInfo.get(0, Long.class) != null && oneWeekInfo.get(0, Long.class) != 0) {
            sellerInfoDTO.setOneWeekCount(oneWeekInfo.get(0, Long.class));
            sellerInfoDTO.setOneWeekPrice(oneWeekInfo.get(1, Integer.class));
        }else {
            sellerInfoDTO.setOneWeekCount(0L);
            sellerInfoDTO.setOneWeekPrice(0);
        }

        if (oneMonthsInfo.get(0, Long.class) != null && oneMonthsInfo.get(0, Long.class) != 0) {
            sellerInfoDTO.setOneMonthsCount(oneMonthsInfo.get(0, Long.class));
            sellerInfoDTO.setOneMonthsPrice(oneMonthsInfo.get(1, Integer.class));
        }else {
            sellerInfoDTO.setOneMonthsCount(0L);
            sellerInfoDTO.setOneMonthsPrice(0);
        }

        if (allInfo.get(0, Long.class) != null && allInfo.get(0, Long.class) != 0) {
            sellerInfoDTO.setAllCount(allInfo.get(0, Long.class));
            sellerInfoDTO.setAllPrice(allInfo.get(1, Integer.class));
        }else {
            sellerInfoDTO.setAllCount(0L);
            sellerInfoDTO.setAllPrice(0);
        }

        sellerInfoDTO.setStatusCountMap(statusCountMap);
        return sellerInfoDTO;
    }

    // 관리자 메인페이지 - 홈 출력 정보 조회 (기간별 주문 건수 & 주문 금액 & 배송 현황 집계) //
    @Override
    public SellerInfoDTO selectAdminInfo(){

        // 필요한 정보를 한번에 처리할 DTO 생성
        SellerInfoDTO sellerInfoDTO = new SellerInfoDTO();

        // 신규 주문 건수 & 신규 주문 금액 (하루)
        LocalDate oneDay = LocalDate.now().minusDays(1);
        Tuple oneDayInfo = jpaQueryFactory
                .select(qOrderDetail.count(), qOrderDetail.detailPrice.sum())
                .from(qOrderDetail)
                .where(qOrderDetail.detailDate.between(oneDay, LocalDate.now()))
                .fetchOne();

        // 주간 주문 건수 & 주간 주문 금액 (최근 일주일)
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        Tuple oneWeekInfo = jpaQueryFactory
                .select(qOrderDetail.count(), qOrderDetail.detailPrice.sum())
                .from(qOrderDetail)
                .where(qOrderDetail.detailDate.between(oneWeekAgo, LocalDate.now()))
                .fetchOne();

        // 월간 주문 건수 & 월간 주문 금액 (최근 한달)
        LocalDate oneMonthsAgo = LocalDate.now().minusMonths(1);
        Tuple oneMonthsInfo = jpaQueryFactory
                .select(qOrderDetail.count(), qOrderDetail.detailPrice.sum())
                .from(qOrderDetail)
                .where(qOrderDetail.detailDate.between(oneMonthsAgo, LocalDate.now()))
                .fetchOne();

        // 전체 주문 건수 & 전체 주문 금액
        Tuple allInfo = jpaQueryFactory
                .select(qOrderDetail.count(), qOrderDetail.detailPrice.sum())
                .from(qOrderDetail)
                .fetchOne();

        // 각 status별 개수 조회
        List<Tuple> allStatus = jpaQueryFactory
                .select(qOrderDetail.detailStatus, qOrderDetail.count())
                .from(qOrderDetail)
                .groupBy(qOrderDetail.detailStatus)
                .fetch();

        // 각 status별 개수를 Key-Value값으로 저장
        Map<String, Long> statusCountMap = new HashMap<>();
        for (Tuple tuple : allStatus) {
            String status = tuple.get(0, String.class);
            Long count = tuple.get(1, Long.class);
            statusCountMap.put(status, count);
        }

        // 지금까지 조회한 정보를 하나의 DTO에 넣어서 반환
        if (oneDayInfo.get(0, Long.class) != null && oneDayInfo.get(0, Long.class) != 0) {
            sellerInfoDTO.setOneDayCount(oneDayInfo.get(0, Long.class));
            sellerInfoDTO.setOneDayPrice(oneDayInfo.get(1, Integer.class));
        }else {
            sellerInfoDTO.setOneDayCount(0L);
            sellerInfoDTO.setOneDayPrice(0);
        }

        if (oneWeekInfo.get(0, Long.class) != null && oneWeekInfo.get(0, Long.class) != 0) {
            sellerInfoDTO.setOneWeekCount(oneWeekInfo.get(0, Long.class));
            sellerInfoDTO.setOneWeekPrice(oneWeekInfo.get(1, Integer.class));
        }else {
            sellerInfoDTO.setOneWeekCount(0L);
            sellerInfoDTO.setOneWeekPrice(0);
        }

        if (oneMonthsInfo.get(0, Long.class) != null && oneMonthsInfo.get(0, Long.class) != 0) {
            sellerInfoDTO.setOneMonthsCount(oneMonthsInfo.get(0, Long.class));
            sellerInfoDTO.setOneMonthsPrice(oneMonthsInfo.get(1, Integer.class));
        }else {
            sellerInfoDTO.setOneMonthsCount(0L);
            sellerInfoDTO.setOneMonthsPrice(0);
        }

        if (allInfo.get(0, Long.class) != null && allInfo.get(0, Long.class) != 0) {
            sellerInfoDTO.setAllCount(allInfo.get(0, Long.class));
            sellerInfoDTO.setAllPrice(allInfo.get(1, Integer.class));
        }else {
            sellerInfoDTO.setAllCount(0L);
            sellerInfoDTO.setAllPrice(0);
        }

        sellerInfoDTO.setStatusCountMap(statusCountMap);
        return sellerInfoDTO;
    }

    // 판매자 관리페이지 - 상품목록 - 상품관리 (전체 상품 목록)
    public Page<Tuple> selectProductForSeller(String prodSeller, ProductPageRequestDTO productPageRequestDTO, Pageable pageable) {

        QueryResults<Tuple> selectProducts = null;
        if (prodSeller.equals("ADMIN")){
            // product - productImg 테이블 join / 판매자 아이디와 일치하는 경우 최신순
            selectProducts = jpaQueryFactory
                    .select(qProduct, qProductimg.thumb190)
                    .from(qProduct)
                    .join(qProductimg)
                    .on(qProduct.prodNo.eq(qProductimg.prodNo))
                    .orderBy(qProduct.prodRdate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }else {
            // product - productImg 테이블 join / 판매자 아이디와 일치하는 경우 최신순
            selectProducts = jpaQueryFactory
                    .select(qProduct, qProductimg.thumb190)
                    .from(qProduct)
                    .join(qProductimg)
                    .on(qProduct.prodNo.eq(qProductimg.prodNo))
                    .where(qProduct.prodSeller.eq(prodSeller))
                    .orderBy(qProduct.prodRdate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }

        List<Tuple> productsResults = selectProducts.getResults();
        long total = selectProducts.getTotal();
        log.info("total : " + total);

        return new PageImpl<>(productsResults, pageable, total);
    }

    // 판매자 관리페이지 - 상품목록 - 상품관리 (검색 상품 목록)
    public Page<Tuple> searchProductForSeller(String prodSeller, ProductPageRequestDTO productPageRequestDTO, Pageable pageable) {

        String type = productPageRequestDTO.getType();
        String keyword = productPageRequestDTO.getKeyword();
        BooleanExpression expression = null;

        // 검색 종류에 따른 where절 표현식 생성
        if(type.equals("prodName")){
            expression = qProduct.prodName.contains(keyword);

        }else if(type.equals("prodNo")){
            expression = qProduct.prodNo.eq(Integer.parseInt(keyword));

        }else if(type.equals("prodCompany")){
            expression = qProduct.prodCompany.contains(keyword);

        }else if(type.equals("sellerNo")){
            expression = qProduct.prodSeller.contains(keyword);
        }

        QueryResults<Tuple> selectProducts = null;
        if (prodSeller.equals("ADMIN")){
            selectProducts = jpaQueryFactory
                    .select(qProduct, qProductimg.thumb190)
                    .from(qProduct)
                    .join(qProductimg)
                    .on(qProduct.prodNo.eq(qProductimg.prodNo))
                    .where(expression)
                    .orderBy(qProduct.prodRdate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        } else {
            // product - productImg 테이블 join / 판매자 아이디와 일치하는 경우 / type과 keyword가 일치하는 경우 / 최신순
            selectProducts = jpaQueryFactory
                    .select(qProduct, qProductimg.thumb190)
                    .from(qProduct)
                    .join(qProductimg)
                    .on(qProduct.prodNo.eq(qProductimg.prodNo))
                    .where(qProduct.prodSeller.eq(prodSeller))
                    .where(expression)
                    .orderBy(qProduct.prodRdate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }

        List<Tuple> productsResults = selectProducts.getResults();
        long total = selectProducts.getTotal();
        return new PageImpl<>(productsResults, pageable, total);
    }

    // 판매자 관리페이지 - 주문관리 - 주문현황 (최근 한달 주문 건수 for 그래프)
    // 판매자 관리페이지 - 주문관리 - 매출현황 (최근 한달 주문 금액 for 그래프)
    public List<OrderDetail> selectSalesForMonth(String prodSeller) {
        LocalDate oneMonthsAgo = LocalDate.now().minusMonths(1);

        if (prodSeller.equals("ADMIN")) {
            return jpaQueryFactory
                    .selectFrom(qOrderDetail)
                    .where(qOrderDetail.detailDate.between(oneMonthsAgo, LocalDate.now()))
                    .orderBy(qOrderDetail.detailDate.desc())
                    .fetch();
        }else {
            return jpaQueryFactory
                    .selectFrom(qOrderDetail)
                    .where(qOrderDetail.prodSeller.eq(prodSeller))
                    .where(qOrderDetail.detailDate.between(oneMonthsAgo, LocalDate.now()))
                    .orderBy(qOrderDetail.detailDate.desc())
                    .fetch();
        }
    }
    
    // 판매자 관리페이지 - 주문관리 - 주문현황 (주문 상품 정보 출력)
    public Page<Tuple> selectProdSalesInfo(String prodSeller, PageRequestDTO pageRequestDTO, Pageable pageable) {

        QueryResults<Tuple> selectOrders = null;
        BooleanExpression expression = null;

        if (pageRequestDTO.getKeyword() != null) {
            log.info("keyword : " + pageRequestDTO.getKeyword());
            if (pageRequestDTO.getKeyword().equals("waiting")){
                expression = qOrderDetail.detailStatus.contains("입금대기");
            }else if (pageRequestDTO.getKeyword().equals("ready")){
                expression = qOrderDetail.detailStatus.contains("배송준비");
            }else if (pageRequestDTO.getKeyword().equals("doing")){
                expression = qOrderDetail.detailStatus.contains("배송중");
            }else if (pageRequestDTO.getKeyword().equals("finish")){
                expression = qOrderDetail.detailStatus.contains("배송완료");
            }else if (pageRequestDTO.getKeyword().equals("cancel")){
                expression = qOrderDetail.detailStatus.contains("취소요청");
            }else if (pageRequestDTO.getKeyword().equals("exchange")){
                expression = qOrderDetail.detailStatus.contains("교환요청");
            }else if (pageRequestDTO.getKeyword().equals("refund")){
                expression = qOrderDetail.detailStatus.contains("환불요청");
            }
        }

        if (pageRequestDTO.getKeyword() != null && !prodSeller.equals("ADMIN")) {
            selectOrders = jpaQueryFactory
                    .select(qOrderDetail, qProduct.prodName, qOrders)
                    .from(qOrderDetail)
                    .join(qProduct)
                    .on(qOrderDetail.prodNo.eq(qProduct.prodNo))
                    .join(qOrders)
                    .on(qOrderDetail.orderNo.eq(qOrders.orderNo))
                    .where(qOrderDetail.prodSeller.eq(prodSeller))
                    .where(expression)
                    .orderBy(qOrderDetail.detailDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }else if(pageRequestDTO.getKeyword() != null && prodSeller.equals("ADMIN")) {
            selectOrders = jpaQueryFactory
                    .select(qOrderDetail, qProduct.prodName, qOrders)
                    .from(qOrderDetail)
                    .join(qProduct)
                    .on(qOrderDetail.prodNo.eq(qProduct.prodNo))
                    .join(qOrders)
                    .on(qOrderDetail.orderNo.eq(qOrders.orderNo))
                    .where(expression)
                    .orderBy(qOrderDetail.detailDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }else if(pageRequestDTO.getKeyword() == null && !prodSeller.equals("ADMIN")) {
            selectOrders = jpaQueryFactory
                    .select(qOrderDetail, qProduct.prodName, qOrders)
                    .from(qOrderDetail)
                    .join(qProduct)
                    .on(qOrderDetail.prodNo.eq(qProduct.prodNo))
                    .join(qOrders)
                    .on(qOrderDetail.orderNo.eq(qOrders.orderNo))
                    .where(qOrderDetail.prodSeller.eq(prodSeller))
                    .orderBy(qOrderDetail.detailDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }else if(pageRequestDTO.getKeyword() == null && prodSeller.equals("ADMIN")) {
            selectOrders = jpaQueryFactory
                    .select(qOrderDetail, qProduct.prodName, qOrders)
                    .from(qOrderDetail)
                    .join(qProduct)
                    .on(qOrderDetail.prodNo.eq(qProduct.prodNo))
                    .join(qOrders)
                    .on(qOrderDetail.orderNo.eq(qOrders.orderNo))
                    .orderBy(qOrderDetail.detailDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }

        List<Tuple> orderResults = selectOrders.getResults();
        long total = selectOrders.getTotal();
        return new PageImpl<>(orderResults, pageable, total);
    }

    // 판매자 관리페이지 - 주문관리 - 매출현황 (전체 기간 매출 요약)
    public List<OrderDetail> selectSalesForTotal(String prodSeller) {
        if (prodSeller.equals("ADMIN")){
            return jpaQueryFactory
                    .selectFrom(qOrderDetail)
                    .orderBy(qOrderDetail.detailDate.desc())
                    .fetch();
        }else {
            return jpaQueryFactory
                    .selectFrom(qOrderDetail)
                    .where(qOrderDetail.prodSeller.eq(prodSeller))
                    .orderBy(qOrderDetail.detailDate.desc())
                    .fetch();
        }
    }

    // 판매자 관리페이지 - 주문관리 - 매출현황 (최근 일년 매출 요약)
    public List<OrderDetail> selectSalesForYear(String prodSeller) {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        if (prodSeller.equals("ADMIN")){
            return jpaQueryFactory
                    .selectFrom(qOrderDetail)
                    .where(qOrderDetail.detailDate.between(oneYearAgo, LocalDate.now()))
                    .orderBy(qOrderDetail.detailDate.desc())
                    .fetch();
        }else {
            return jpaQueryFactory
                    .selectFrom(qOrderDetail)
                    .where(qOrderDetail.prodSeller.eq(prodSeller))
                    .where(qOrderDetail.detailDate.between(oneYearAgo, LocalDate.now()))
                    .orderBy(qOrderDetail.detailDate.desc())
                    .fetch();
        }
    }

    // 판매자 관리페이지 - 주문관리 - 매출현황 (최근 일주일 매출 요약)
    public List<OrderDetail> selectSalesForWeek(String prodSeller) {
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        if (prodSeller.equals("ADMIN")) {
            return jpaQueryFactory
                    .selectFrom(qOrderDetail)
                    .where(qOrderDetail.detailDate.between(oneWeekAgo, LocalDate.now()))
                    .orderBy(qOrderDetail.detailDate.desc())
                    .fetch();
        }else {
            return jpaQueryFactory
                    .selectFrom(qOrderDetail)
                    .where(qOrderDetail.prodSeller.eq(prodSeller))
                    .where(qOrderDetail.detailDate.between(oneWeekAgo, LocalDate.now()))
                    .orderBy(qOrderDetail.detailDate.desc())
                    .fetch();
        }
    }

    @Transactional
    public long updateSellerNameByUserId(String userId, String sellerName) {
        try{
            long result = jpaQueryFactory
                    .update(qSeller)
                    .set(qSeller.sellerName, sellerName)
                    .where(qSeller.userId.eq(userId))
                    .execute();
            //성공시 1로 반환
            return result;
        }catch(Exception e){
            log.error("error msg :" + e.getMessage());
            return -1;
        }
    }

    @Transactional
    public long updateSellerHpByUserId(String userId, String sellerHp) {
        try{
            long result = jpaQueryFactory
                    .update(qSeller)
                    .set(qSeller.sellerHp, sellerHp)
                    .where(qSeller.userId.eq(userId))
                    .execute();
            return result;
        }catch(Exception e){
            log.error("error msg: " + e.getMessage());
            return -1;
        }
    }
/*
    @Transactional
    public long updateSellerFaxByUserId(String userId, String fax) {
        try{
            long result = jpaQueryFactory
                    .update(qSeller)
                    .set(qSeller.fax, fax)
                    .where(qSeller.userId.eq(userId))
                    .execute();
            return result;
        }catch(Exception e){
            log.error("error msg: " + e.getMessage());
            return -1;
        }
    }*/

    // 관리자 - 상점관리 - 판매자현황 조회
    public PageResponseDTO selectSellerList(Pageable pageable, PageRequestDTO pageRequestDTO) {
        // 검색 여부에 따라 Seller 조회
        BooleanExpression expression = null;
        QueryResults<Seller> sellerList = null;
        if (pageRequestDTO.getKeyword() != null) {
            if (pageRequestDTO.getType().equals("sellerNo")){
                expression = qSeller.sellerNo.contains(pageRequestDTO.getKeyword());
            }else if (pageRequestDTO.getType().equals("company")){
                expression = qSeller.company.contains(pageRequestDTO.getKeyword());
            }else if (pageRequestDTO.getType().equals("sellerName")){
                expression = qSeller.sellerName.contains(pageRequestDTO.getKeyword());
            }
            sellerList = jpaQueryFactory
                    .selectFrom(qSeller)
                    .where(expression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }else {
            sellerList = jpaQueryFactory
                    .selectFrom(qSeller)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }
        // seller Entity -> SellerDTO로 변환
        List<SellerDTO> sellerDTOList = new ArrayList<>();
        for (Seller eachSeller : sellerList.getResults()) {
            SellerDTO sellerDTO = modelMapper.map(eachSeller, SellerDTO.class);

            // 각 Seller별 총 주문 수량, 총 주문 금액 조회
            Tuple salesInfo = jpaQueryFactory
                    .select(qOrderDetail.count(), qOrderDetail.detailPrice.sum())
                    .from(qOrderDetail)
                    .where(qOrderDetail.prodSeller.eq(eachSeller.getSellerNo()))
                    .fetchOne();
            Long prodCount = jpaQueryFactory
                    .select(qProduct.count())
                    .from(qProduct)
                    .where(qProduct.prodSeller.eq(eachSeller.getSellerNo()))
                    .fetchOne();

            if (salesInfo.get(0, Long.class) != 0 && salesInfo.get(1, BigDecimal.class) != null) {
                sellerDTO.setSellerCount(salesInfo.get(0, Long.class));
                sellerDTO.setSellerSum(salesInfo.get(1, Integer.class));
                sellerDTO.setProdCount(prodCount);
            }else {
                sellerDTO.setSellerCount(0L);
                sellerDTO.setSellerSum(0);
                sellerDTO.setProdCount(prodCount);
            }
            sellerDTOList.add(sellerDTO);
        }

        long total = sellerList.getTotal();
        Page<SellerDTO> PageSeller = new PageImpl<>(sellerDTOList, pageable, total);

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(sellerDTOList)
                .total((int) PageSeller.getTotalElements())
                .build();
    }
}
