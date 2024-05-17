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
@Table(name = "pdreviewimg")
public class PdReviewImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int revImgNo;
    private int revNo;
    private String revThumb;

}
