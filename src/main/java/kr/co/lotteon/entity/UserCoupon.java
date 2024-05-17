package kr.co.lotteon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name="usercoupon")
public class UserCoupon {
    @Id
    private int couponId;
    private String userId;
    private int cpNo;
    @CreationTimestamp
    private LocalDateTime ucpDate;
    private LocalDateTime ucpUseDate;
    private String ucpStatus;
}
