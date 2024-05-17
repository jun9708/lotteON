package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.custom.OrdersRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class OrdersRepositoryImpl implements OrdersRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QOrders qOrders = QOrders.orders;
    private final QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final QProductimg qProductimg = QProductimg.productimg;
    private final ModelMapper modelMapper;


    // My/Order 페이지 상품 목록조회(날짜순)
    public MyOrderPageResponseDTO selectMyOrdersByDate(String UserId,Pageable pageable,MyOrderPageRequestDTO myOrderPageRequestDTO){
        log.info("IMPL 시작");
        // 0. 카테고리에 따른 where절 작성
        // 1. orders테이블에서 10개 조회
        // SELECT orderNo FROM orders WHERE userId = '?' ORDER BY orderDate DESC LIMIT 10
        long total = 0;
        BooleanExpression expression = null; //where절 보관용
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);//일주일전
        LocalDate oneMonthsAgo = LocalDate.now().minusMonths(1);//한달전
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);//3개월전
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);//6개월전
        LocalDate yearOneAgo = LocalDate.now().minusYears(1);//1년전

        List<Integer> selectOrderNo = new ArrayList<>();
        if (myOrderPageRequestDTO.getCate() != null){
            // 날짜로 검색하는 경우
            if(myOrderPageRequestDTO.getCate().equals("week")) {
                expression = qOrders.orderDate.between(oneWeekAgo.atStartOfDay(), LocalDate.now().atStartOfDay());
            }else if(myOrderPageRequestDTO.getCate().equals("month")) {
                expression = qOrders.orderDate.between(oneMonthsAgo.atStartOfDay(), LocalDate.now().atStartOfDay());
            }else if(myOrderPageRequestDTO.getCate().equals("3month")){
                expression = qOrders.orderDate.between(threeMonthsAgo.atStartOfDay(), LocalDate.now().atStartOfDay());
            }else if(myOrderPageRequestDTO.getCate().equals("6month")){
                expression = qOrders.orderDate.between(sixMonthsAgo.atStartOfDay(), LocalDate.now().atStartOfDay());
            }else if(myOrderPageRequestDTO.getCate().equals("year")){
                expression = qOrders.orderDate.between(yearOneAgo.atStartOfDay(), LocalDate.now().atStartOfDay());
            }else if(myOrderPageRequestDTO.getCate().equals("custom")){
                // 시작날짜
                // 마지막날짜
                LocalDate startDate = myOrderPageRequestDTO.getStartDate();
                LocalDate finalDate = myOrderPageRequestDTO.getFinalDate();
                expression = qOrders.orderDate.between(startDate.atStartOfDay(), finalDate.atStartOfDay());
            }
            selectOrderNo = jpaQueryFactory
                    .select(qOrders.orderNo)
                    .from(qOrders)
                    .where(qOrders.userId.eq(UserId))
                    .where(expression)
                    .orderBy(qOrders.orderDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            total = jpaQueryFactory.select(qOrders.count()).from(qOrders).where(qOrders.userId.eq(UserId))
                    .where(expression).orderBy(qOrders.orderDate.desc()).fetchOne();
        }else {
            // 날짜로 검색이 아닌경우
            selectOrderNo = jpaQueryFactory
                    .select(qOrders.orderNo)
                    .from(qOrders)
                    .where(qOrders.userId.eq(UserId))
                    .orderBy(qOrders.orderDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            total = jpaQueryFactory.select(qOrders.count()).from(qOrders).where(qOrders.userId.eq(UserId))
                    .orderBy(qOrders.orderDate.desc()).fetchOne();
        }
        log.info("orderNo 조회" + selectOrderNo);

        //2. 그 10개의 orderNo를 for문으로 orderdetail에서 조회 -> 각 orderNo 마다 List<orderDetail>
        //SELECT * FROM orderdetail AS a JOIN product AS b ON a.prodNo = b.prodNo WHERE a.orderNO = '?'

        LinkedHashMap<Integer, List<OrderDetailDTO>> orderDetailDTOMap = new LinkedHashMap<>();

        for(Integer orderNo : selectOrderNo){
            List<Tuple> orderDetails  = jpaQueryFactory
                    .select(qOrderDetail, qProduct.prodCompany, qProduct.prodName, qProductimg.thumb190)
                    .from(qOrderDetail)
                    .join(qProduct)
                    .on(qOrderDetail.prodNo.eq(qProduct.prodNo))
                    .join(qProductimg)
                    .on(qProduct.prodNo.eq(qProductimg.prodNo))
                    .where(qOrderDetail.orderNo.eq(orderNo))
                    .fetch();

            log.info("OrderDetail 조회" +orderDetails);

            // List<Tuple> => List<OrderDetailDTO>
            List<OrderDetailDTO> dtoList = orderDetails.stream()
                    .map(tuple -> {
                        OrderDetail orderDetail = tuple.get(0, OrderDetail.class);
                        //구조체로 가져온게 아니라 단일값을 가져온거라 String형으로 선언
                        String company = tuple.get(1, String.class); // String으로 형변환을 하겠다
                        String prodName = tuple.get(2, String.class);
                        String thumb190 = tuple.get(3, String.class);

                        //OrderDetailDTO 로 변환
                        OrderDetailDTO orderDetailDTO = modelMapper.map(orderDetail, OrderDetailDTO.class);
                        orderDetailDTO.setProdCompany(company);
                        orderDetailDTO.setProdName(prodName);
                        orderDetailDTO.setThumb190(thumb190);

                        return orderDetailDTO;
                    }
                ).toList();

            log.info("dtoList" +dtoList);
            orderDetailDTOMap.put(orderNo,dtoList);
        }
        log.info("orderDetailDTOMap" +orderDetailDTOMap);

        // 페이징 처리
        Page<Integer> pageImpl = new PageImpl<>(selectOrderNo, pageable, total);
        int total2 = (int) pageImpl.getTotalElements();

        return MyOrderPageResponseDTO.builder()
                .pageRequestDTO(myOrderPageRequestDTO)
                .myOrderDTOList(orderDetailDTOMap)
                .total(total2)
                .build();
    }

    @Override
    public LinkedHashMap<Integer, List<OrderDetailDTO>> selectMyOrdersHome(String UserId) {
        
        //OrderNo 조회하기
       List<Integer> selectMyOrderNo = jpaQueryFactory
                                        .select(qOrders.orderNo)
                                        .from(qOrders)
                                        .where(qOrders.userId.eq(UserId))
                                        .orderBy(qOrders.orderDate.desc())
                                        .limit(2)
                                        .fetch();
       log.info("orderNo 조회하기 : " +selectMyOrderNo);

       LinkedHashMap<Integer, List<OrderDetailDTO>> myOrderDetailDTOMap = new LinkedHashMap<>();
       for(Integer orderNo : selectMyOrderNo){

           List<Tuple> detailList = jpaQueryFactory
                                       .select(qOrderDetail, qProduct.prodCompany, qProduct.prodName, qProductimg.thumb190)
                                       .from(qOrderDetail)
                                       .join(qProduct)
                                       .on(qOrderDetail.prodNo.eq(qProduct.prodNo))
                                       .join(qProductimg)
                                       .on(qProduct.prodNo.eq(qProductimg.prodNo))
                                       .where(qOrderDetail.orderNo.eq(orderNo))
                                       .fetch();
           log.info("detailList : " +detailList);

           //List<Tuple> => List<OrderDetailDTO>
           List<OrderDetailDTO> dtoList = detailList.stream()
                   .map(tuple -> {
                       OrderDetail orderDetail = tuple.get(0, OrderDetail.class);
                       String company = tuple.get(1, String.class); // String으로 형변환을 하겠다
                       String prodName = tuple.get(2, String.class);
                       String thumb190 = tuple.get(3, String.class);

                       //OrderDetailDTO 로 변환
                       OrderDetailDTO orderDetailDTO = modelMapper.map(orderDetail, OrderDetailDTO.class);
                       orderDetailDTO.setProdCompany(company);
                       orderDetailDTO.setProdName(prodName);
                       orderDetailDTO.setThumb190(thumb190);

                       return orderDetailDTO;
                     }
                   ).toList();
           myOrderDetailDTOMap.put(orderNo,dtoList);
       }

        return myOrderDetailDTOMap;
    }

}
