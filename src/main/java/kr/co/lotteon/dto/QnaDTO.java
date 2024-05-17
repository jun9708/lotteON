package kr.co.lotteon.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaDTO {
    private Integer qnaNo;
    private String userId;
    private String qnaTitle;
    private String qnaContent;
    private LocalDateTime qnaDate;
    private String qnaType;
    private String qnaCate;
    private String qnaStatus;
    private String qnaReply;
}
