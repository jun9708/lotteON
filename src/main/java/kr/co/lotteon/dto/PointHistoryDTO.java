package kr.co.lotteon.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistoryDTO {

    private int pointHisNo;
    private int pointNo;
    private int changePoint;
    private LocalDateTime changeDate;
    private String changeCode;
    private String changeType;

    private String cate;
    private LocalDate startDate;
    private LocalDate finalDate;

}

