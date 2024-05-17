package kr.co.lotteon.repository;

import kr.co.lotteon.entity.PdReview;
import kr.co.lotteon.repository.custom.PdReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdReviewRepository extends JpaRepository<PdReview, Integer>, PdReviewRepositoryCustom {
}
