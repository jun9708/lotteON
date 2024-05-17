package kr.co.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name="prodqna")
public class ProdQna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prodQnaNo;
    private String userId;
    private int prodNo;
    private String prodQnaTitle;
    private String prodQnaContent;
    @CreationTimestamp
    private LocalDateTime prodQnaDate;
    private String prodQnaSecret;
    private String prodQnaStatus;
}
