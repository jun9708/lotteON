package kr.co.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name="companystory")
public class CompanyStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storyNo;
    private String storyName;
    private String storyContent;
    private String storyImg;
    private String storyCate;
    private LocalDate storyDate;
}
