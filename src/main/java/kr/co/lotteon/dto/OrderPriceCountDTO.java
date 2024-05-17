package kr.co.lotteon.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrderPriceCountDTO {
    private int salesPrice;
    private int salesCount;
    private int cancelPrice;
    private int cancelCount;
    private int exchangePrice;
    private int exchangeCount;
    private int refundPrice;
    private int refundCount;
}
