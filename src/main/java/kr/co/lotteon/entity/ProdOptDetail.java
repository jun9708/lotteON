package kr.co.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "prodoptdetail")
public class ProdOptDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int optDetailNo;
    public int prodNo;
    public int optNo1;
    public int optNo2;
    public int optNo3;
    public int optPrice;
    public int optStock;

    //추가 : optValue 필드
    @Transient
    private String optValue1;
    @Transient
    private String optValue2;
    @Transient
    private String optValue3;
}
