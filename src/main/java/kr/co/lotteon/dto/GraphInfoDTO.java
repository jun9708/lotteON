package kr.co.lotteon.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GraphInfoDTO {

    private LocalDate date;
    private int count;
    private int price;
}