package kr.co.lotteon.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDTO {

    private String sellerNo;
    private String company;
    private String userId;
    private String sellerName;
    private String sellerHp;
    private String businessNum;
    private String licenseNum;
    private String sellerGrade;
    private String fax;

    // 관리자 - 상점관리 - 판매자현황을 위한 추가 필드
    private Long sellerCount;
    private int sellerSum;
    private Long prodCount;
}
