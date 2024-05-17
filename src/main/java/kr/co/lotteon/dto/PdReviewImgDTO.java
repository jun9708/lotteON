package kr.co.lotteon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PdReviewImgDTO {
    private int revImgNo;
    private int revNo;
    private String revThumb;
}
