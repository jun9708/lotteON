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
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeNo;
    private String noticeTitle;
    private String noticeContent;

    @CreationTimestamp
    private LocalDateTime noticeDate;
    private String noticeType;
    private String noticeCate;

    private int noticeHit;
}
