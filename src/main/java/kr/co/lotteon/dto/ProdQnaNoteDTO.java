package kr.co.lotteon.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdQnaNoteDTO {
    private int cQnaNo;
    private int prodQnaNo;
    private String sellerNo;
    private String content;
    private LocalDateTime cQnaDate;

    private int prodNo;
    private String prodName;
    private String thumb190;
}
