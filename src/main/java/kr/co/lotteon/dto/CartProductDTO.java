package kr.co.lotteon.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartProductDTO {
    private int cartProdNo;
    private int cartNo;
    private int prodNo;
    private int optNo;
    private int count;
}
