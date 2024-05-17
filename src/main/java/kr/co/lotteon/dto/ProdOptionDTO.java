package kr.co.lotteon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProdOptionDTO {
    public int optNo;
    public int prodNo;
    public String optName;
    public String optValue;
}
