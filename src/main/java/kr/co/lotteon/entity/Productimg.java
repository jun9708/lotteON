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
@Table(name="productimg")
public class Productimg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imgNo;
    private int prodNo;
    private String thumb190;
    private String thumb230;
    private String thumb456;
    private String thumb940;
 }
