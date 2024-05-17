package kr.co.lotteon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name="user")
public class User {
    @Id
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

    @Transient
    private int pointNo;
    @Transient
    private int pointBalance;
 }
