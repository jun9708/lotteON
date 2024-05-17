package kr.co.lotteon.dto;

import kr.co.lotteon.entity.OrderDetail;
import kr.co.lotteon.entity.Orders;
import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrderInfoDTO {

    private List<Map<String, String>> orderDetails;
    private Map<String, String> orders;
}
