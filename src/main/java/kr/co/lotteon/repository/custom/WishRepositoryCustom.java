package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WishRepositoryCustom {
    // my - wish 조회
    public Page<Tuple> selectUserWish(String userId, PageRequestDTO pageRequestDTO, Pageable pageable);

    // 상품 옵션 이름 조회
    public String selectProdOption(int optDetailNo);
}