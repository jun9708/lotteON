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
@Table(name = "prodoption")
public class ProdOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int optNo;
    public int prodNo;
    public String optName;
    public String optValue;
}
