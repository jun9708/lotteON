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
@Table(name="addr")
public class Addr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addrNo;
    private String userId;
    private String addrName;
    private String receiver;
    private String hp;
    private int zip;
    private String addr1;
    private String addr2;
    private String defaultAddr;
}
