package kr.co.lotteon.dto;

import kr.co.lotteon.entity.PointHistory;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PdReviewDTO {
    private int revNo;
    private String userId;
    private int prodNo;
    private String revContent;
    private int revScore;
    private LocalDateTime revAddDate;

    //OrderDetail
    private String detailNo;

    //PointHistory
    private List<PointHistory> pointHistoriList;

    //PdReviewImg
    private int revImgNo;
    private String revThumb;
    private String prodName;

    private String cateCode;
}
