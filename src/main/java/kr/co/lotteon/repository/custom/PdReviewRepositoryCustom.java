package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.entity.PdReview;
import kr.co.lotteon.entity.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PdReviewRepositoryCustom {
    public PageResponseDTO selectReviews(String userId, Pageable pageable, PageRequestDTO pageRequestDTO);
    // 상품 리뷰 조회
    public Page<Tuple> selectProdReviewForView(int prodNo, Pageable pageable, PageRequestDTO pageRequestDTO);
}
