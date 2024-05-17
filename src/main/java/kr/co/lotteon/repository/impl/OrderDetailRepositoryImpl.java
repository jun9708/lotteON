package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.OrderDetailDTO;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.custom.OrderDetailRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class OrderDetailRepositoryImpl implements OrderDetailRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final QProduct qProduct = QProduct.product;
    private final QProductimg qProductimg = QProductimg.productimg;
    private final QOrders qOrders = QOrders.orders;
    private final ModelMapper modelMapper;

    public Map<String, List<?>> selectDetailCheck(int orderNo, OrderDetailDTO dto) {
         QueryResults<Tuple> selectOrderDetail = jpaQueryFactory
                                                        .select(qOrderDetail, qProduct, qProductimg)
                                                        .from(qOrderDetail)
                                                        .join(qProduct)
                                                        .on(qOrderDetail.prodNo.eq(qProduct.prodNo))
                                                        .join(qProductimg)
                                                        .on(qProductimg.prodNo.eq(qProduct.prodNo ))
                                                        .where(qOrderDetail.orderNo.eq(orderNo))
                                                        .fetchResults();

         List<Orders> orderNoInfo = jpaQueryFactory
                                         .selectFrom(qOrders)
                                         .where(qOrders.orderNo.eq(orderNo))
                                         .fetch();

        List<Tuple> selectOrderList = selectOrderDetail.getResults();

        List<OrderDetailDTO> orderDetailDTOList = selectOrderList.stream()
                .map(tuple -> {
                        OrderDetail orderDetail = tuple.get(0, OrderDetail.class);
                        Product product = tuple.get(1, Product.class);
                        Productimg productimg = tuple.get(2, Productimg.class);

                        OrderDetailDTO orderDetailDTO = modelMapper.map(orderDetail, OrderDetailDTO.class);
                        orderDetailDTO.setThumb190(productimg.getThumb190());
                        orderDetailDTO.setProdName(product.getProdName());
                        orderDetailDTO.setProdCompany(product.getProdCompany());

                        return orderDetailDTO;

                        }).toList();

        Map<String, List<?>> resultMap = new HashMap<>();
        resultMap.put("orderNoInfo", orderNoInfo);
        resultMap.put("orderDetailDTOList", orderDetailDTOList);




        log.info("selectOrderList : " +selectOrderList);

        return resultMap;



    }


}
