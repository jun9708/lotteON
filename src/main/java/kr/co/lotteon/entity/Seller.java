package kr.co.lotteon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name="seller")
public class Seller {

    @Id
    private String sellerNo;
    private String userId;
    private String company;
    private String sellerName;
    private String sellerHp;
    private String businessNum;
    private String licenseNum;
    private String sellerGrade;
    private String fax;

}
