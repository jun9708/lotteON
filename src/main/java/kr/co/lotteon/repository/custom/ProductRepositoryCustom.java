package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.ProductPageRequestDTO;
import kr.co.lotteon.entity.ProdOption;
import kr.co.lotteon.entity.Product;
import kr.co.lotteon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepositoryCustom {
    
    // ADMIN 페이지 프로덕트 조회
    public Page<Tuple> selectProducts(ProductPageRequestDTO pageRequestDTO, Pageable pageable);

    // Index 페이지 상품 조회
    public List<Tuple> selectIndexProducts(String sort);
    // cate 로 프로덕트 조회
    public Page<Tuple> selectProductsByCate(ProductPageRequestDTO pageRequestDTO, Pageable pageable);
    public Page<Tuple> searchProductsByCateAndKeyWord(ProductPageRequestDTO pageRequestDTO, Pageable pageable);

    // 프로덕트 상세 조회
    public Tuple selectProduct(int prodNo);

    // cart 물품 조회
    public Tuple selectProductById(int prodNo);

}
