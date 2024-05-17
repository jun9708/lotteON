package kr.co.lotteon.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CartInfoDTO {

    // Cart 출력을 위한 필드
    // Product
    private int prodNo;
    private String prodName;
    private String prodInfo;
    private int prodDiscount;
    private int prodPrice;
    private String thumb190;
    private String prodCompany;
    private String prodSeller;
    private int prodDeliveryFee;

    // ProdOptDetail
    private String optValue1;
    private String optValue2;
    private String optValue3;
    private int optDetailNo;
    private int optStock;
    private int optPrice;

    // CartProduct
    private int count;
    private int cartProdNo;

    private  String userId;

}