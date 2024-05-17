package kr.co.lotteon.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private int cartNo;
    private String userId;
}
