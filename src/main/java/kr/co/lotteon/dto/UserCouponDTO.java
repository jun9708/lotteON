package kr.co.lotteon.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCouponDTO {
    private int couponId;
    private String userId;
    private int cpNo;
    private LocalDateTime ucpDate;
    private LocalDateTime ucpUseDate;
    private String ucpStatus;

    // 추가필드
    private couponsDTO couponsDTO;
}
