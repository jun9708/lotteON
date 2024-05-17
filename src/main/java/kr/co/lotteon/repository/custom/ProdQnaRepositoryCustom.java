package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.entity.ProdQna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProdQnaRepositoryCustom {
    // myProdQna 조회
    public Page<ProdQna> selectMyProdQna(String userId, PageRequestDTO pageRequestDTO, Pageable pageable);
    
    // 판매자 관리페이지 - QNA List - 목록 조회
    public Page<Tuple> selectSellerQnaList(String prodSeller, PageRequestDTO pageRequestDTO, Pageable pageable);

    // 판매자 관리페이지 - QNA View
    public Map<String, Object> selectSellerQnaView(int qnaNo);

    // 관리자 메인페이지 - 고객문의 최신순 5개 조회
    // 판매자 메인페이지 - 고객문의 최신순 5개 조회
    public List<ProdQna> selectProdQnaForIndex(String prodSeller);

    // 상품 문의 조회
    public PageResponseDTO selectProdQna(int prodNo, Pageable pageable, PageRequestDTO pageRequestDTO);
}
