package kr.co.lotteon.dto;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TermsDTO {

    //terms
    private int termsNo;
    private String buyer;
    private String finance;
    private String location;
    private String privacy;
    private String seller;

    // 추가필드
    private String termHead;
    private String termTitle;
    private String termContent;
}
