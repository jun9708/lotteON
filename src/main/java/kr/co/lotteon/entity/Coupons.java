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
@Table(name = "coupons")
public class Coupons {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cpNo;
    private String cpName;
    private int cpDcPrice;
    private String cpInfo;
    private String cpStatus;
    private int cpMinPrice;

    @CreationTimestamp
    private LocalDateTime cpEndDate;

}
