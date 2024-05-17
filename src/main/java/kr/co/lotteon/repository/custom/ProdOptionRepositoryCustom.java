package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.ResponseOptionDTO;
import kr.co.lotteon.entity.ProdOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProdOptionRepositoryCustom {
    // 상품 옵션 조회
    public ResponseOptionDTO selectProductOption(int prodNo);
}
