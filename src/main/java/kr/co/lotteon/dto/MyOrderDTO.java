package kr.co.lotteon.dto;

import lombok.*;

import java.time.LocalDateTime;

// my/order 페이지를 위한 DTO
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyOrderDTO {

    private String prodName;
    private String prodCompany;
    private String thumb190;

    private int count;
    private int orderNo;
    private int detailPrice;

    private String detailStatus;
    private LocalDateTime orderDate;

}
