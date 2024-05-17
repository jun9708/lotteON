package kr.co.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prodNo;
    private String prodName;
    private int prodPrice;
    private int prodDiscount;
    private int prodPoint;
    private int prodStock;
    private String prodInfo;
    private String prodOrg;
    private int prodSold;

    private int tReviewCount;
    private float tReviewScore;
    private int prodHit;

    @CreationTimestamp
    private LocalDateTime prodRdate;
    private String prodCompany;
    private int prodDeliveryFee;
    private String cateCode;
    private String prodStatus;
    private String prodSeller;
    private String prodBusinessType;
    private String prodReceipt;
    private String prodTax;


    // 이미지 출력을 위한 테이블 추가
    @Transient
    private String thumb190;
    @Transient
    private String thumb230;
    @Transient
    private String thumb456;
    @Transient
    private String thumb940;

    @Transient
    private int revScore;
    @Transient
    private String sellerGrade ;

 }
