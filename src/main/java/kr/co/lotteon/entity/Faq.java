package kr.co.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "faq")
public class Faq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int faqNo;
    private String faqTitle;
    private String faqContent;

    @CreationTimestamp
    private LocalDateTime faqDate;
    private String faqType;
    private String faqCate;

    private int faqHit;
}
