package kr.co.lotteon.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddrDTO {
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
