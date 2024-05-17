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
public class SellerInfoDTO {

    // 주문건수, 주문금액 출력을 위한 필드
    private Long oneDayCount;
    private int oneDayPrice;
    private Long oneWeekCount;
    private int oneWeekPrice;
    private Long oneMonthsCount;
    private int oneMonthsPrice;
    private Long allCount;
    private int allPrice;
    private int maxCount;
    private int maxPrice;

    // 주문상태 출력을 위한 필드
    private Map<String, Long> statusCountMap;
    
    // 그래프 출력을 위한 필드 나중에 지우기
    private List<GraphInfoDTO> graphInfoDTO;
}