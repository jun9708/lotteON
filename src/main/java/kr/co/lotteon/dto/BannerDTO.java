package kr.co.lotteon.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BannerDTO {

    private int banNo;
    private String banName;
    private String banLink;

    private String banImgName;
    private String banImgCate;

    private LocalTime banStime;
    private LocalTime banEtime;

    private LocalDate banSdate;
    private LocalDate banEdate;

    private String banUsable;
}
