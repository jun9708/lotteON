package kr.co.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "qna")
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer qnaNo;
    private String userId;
    private String qnaTitle;
    private String qnaContent;

    @CreationTimestamp
    private LocalDateTime qnaDate;
    private String qnaType;
    private String qnaCate;
    @ColumnDefault("검토중")
    private String qnaStatus;
    private String qnaReply;
}
