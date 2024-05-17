package kr.co.lotteon.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDTO {
// 카멜 표기법으로 수정할 것
    private int noticeNo;
    private String noticeTitle;
    private String noticeContent;
    private String noticeType;
    private String noticeCate;
    private int noticeHit;
    private LocalDateTime noticeDate;
}
