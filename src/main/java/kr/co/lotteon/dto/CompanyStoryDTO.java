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
public class CompanyStoryDTO {
    private int storyNo;
    private String storyName;
    private String storyContent;
    private String storyImg;
    private String storyCate;
    private LocalDate storyDate;
}
