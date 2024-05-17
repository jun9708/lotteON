package kr.co.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @CreationTimestamp
    private LocalDateTime orderDate;

    @Transient
    private String userName;
    @Transient
    private String userHp;


}
