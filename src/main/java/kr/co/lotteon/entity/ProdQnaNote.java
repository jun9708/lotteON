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
@Table(name="prodqnanote")
public class ProdQnaNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cQnaNo;
    private int prodQnaNo;
    private String sellerNo;
    private String content;
    @CreationTimestamp
    private LocalDateTime cQnaDate;

    @Transient
    private int prodNo;
    @Transient
    private String prodName;
    @Transient
    private String thumb190;
}
