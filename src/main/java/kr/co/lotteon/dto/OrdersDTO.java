package kr.co.lotteon.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrdersDTO {

    private int orderNo;
    private String userId;
    private String orderReceiver;
    private String orderHp;
    private String orderAddr;
    private String orderPay;
    private int orderPrice;
    private int userUsedPoint;
    private String orderMemo;
    private String orderStatus;
    private LocalDateTime orderDate;

    // 주문완료 페이지 출력위해 추가
    private String userName;
    private String userHp;
}
