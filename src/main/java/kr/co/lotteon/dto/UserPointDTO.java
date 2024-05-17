package kr.co.lotteon.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPointDTO {
    private int pointNo;
    private String userId;
    private int pointBalance;

    private int pointHisNo;
    private String changePoint;

    private LocalDateTime changeDate;
    private String changeCode;
    private String changeType;

    // 관리자 페이지를 위한 추가 필드
    private String userName;
    private List<PointHistoryDTO> historyDTOList;
}
