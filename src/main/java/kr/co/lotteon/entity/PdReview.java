package kr.co.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pdreview")
public class PdReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int revNo;
    private String userId;
    private int prodNo;
    private String revContent;
    private int revScore;

    @CreationTimestamp
    private LocalDateTime revAddDate;
    @Transient
    private String cateCode;

}
