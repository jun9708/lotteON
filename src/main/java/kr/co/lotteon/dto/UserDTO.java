package kr.co.lotteon.dto;

import jakarta.persistence.Transient;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String userId;
    private String userPw;
    private String userName;
    private LocalDate userBirth;
    private String userHp;
    private String userGender;
    private String userEmail;
    private String userGrade;
    private String userRole;
    private String userZip;
    private String userAddr1;
    private String userAddr2;
    private String userPromo;
    private String userStatus;

    @CreationTimestamp
    private LocalDateTime userRegDate;

    private LocalDateTime userVisitDate;
    private String userProvider;
    private LocalDateTime userUpdate;
    private String userProfile;
    private int userPoint;
    
    // Seller 컬럼
    private String sellerId;
    private String businessNum;
    private String company;
    private String sellerHp;
    private String sellerName;
    private String licenseNum;
    private String sellerGrade;
    private String fax;

    private int pointNo;
    private int pointBalance;

}
