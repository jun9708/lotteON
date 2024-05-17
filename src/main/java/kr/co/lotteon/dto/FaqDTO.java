package kr.co.lotteon.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqDTO {
    private int faqNo;
    private String faqTitle;
    private String faqContent;
    private LocalDateTime faqDate;
    private String faqType;
    private String faqCate;
    private int faqHit;
}
