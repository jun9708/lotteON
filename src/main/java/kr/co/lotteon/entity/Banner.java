package kr.co.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "banner")
public class Banner {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int banNo;
    private String banName;
    private String banLink;

    private String banImgName;
    private String banImgCate;

    private LocalTime banStime;
    private LocalTime banEtime;

    private LocalDate banSdate;
    private LocalDate banEdate;

    private String banUsable;
}
