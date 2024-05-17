package kr.co.lotteon.dto;

import kr.co.lotteon.entity.ProdQnaNote;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProdQnaDTO {
    private int prodQnaNo;
    private String userId;
    private int prodNo;
    private String prodQnaTitle;
    private String prodQnaContent;
    private LocalDateTime prodQnaDate;
    private String prodQnaSecret;
    private String prodQnaStatus;
    
    // 판매자 관리페이지 - FAQ List - 목록 조회를 위한 추가 필드
    private String prodName;

    // 답변을 위한 추가 필드
    private List<ProdQnaNoteDTO> ProdQnaNoteList;
}
