package kr.co.lotteon.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class couponsDTO {

    private int cpNo;
    private String cpName;
    private int cpDcPrice;
    private String cpInfo;
    private String cpStatus;
    private int cpMinPrice;
    private LocalDateTime cpEndDate;

}
