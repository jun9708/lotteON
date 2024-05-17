package kr.co.lotteon.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class prodOptDetailDTO {
    public int optDetailNo;
    public int prodNo;
    public int optNo1;
    public int optNo2;
    public int optNo3;
    public int optPrice;
    public int optStock;
}
