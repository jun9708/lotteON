package kr.co.lotteon.repository;

import kr.co.lotteon.entity.CompanyStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompanyStoryRepository extends JpaRepository<CompanyStory, Integer> {
    List<CompanyStory> findByStoryCate(String storyCate);
}
