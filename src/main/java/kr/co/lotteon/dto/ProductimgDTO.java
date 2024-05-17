package kr.co.lotteon.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductimgDTO {
    private int imgNo;
    private int prodNo;
    private String thumb190;
    private String thumb230;
    private String thumb456;
    private String thumb940;

    //이미지 타입 변환을 위한 DTO
    private MultipartFile multThumb190;
    private MultipartFile multThumb230;
    private MultipartFile multThumb456;
    private MultipartFile multThumb940;
}
