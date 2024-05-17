package kr.co.lotteon.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishDTO {
    private int wishNo;
    private String userId;
    private int prodNo;
    private int optNo;
    private LocalDateTime wishRdate;
    private int wishCount;

    // 관심상품 출력을 위한 추가필드
    private String prodName;
    private int prodPrice;
    private String thumb190;
    private String optName;
    private String cateCode;
}
