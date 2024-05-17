package kr.co.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orderdetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailNo;
    private int orderNo;
    private int prodNo;
    private int optNo;
    private int count;
    private int detailPrice;
    private int detailPoint;
    private String detailStatus;
    private String prodSeller;
    @CreationTimestamp
    private LocalDate detailDate;

    @Transient
    private int cartProdNo;
}
