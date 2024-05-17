package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Product;
import kr.co.lotteon.entity.Productimg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductimgRepository extends JpaRepository<Productimg, Integer> {
    // prodNo로 전체 이미지 조회
    public Productimg findByProdNo(int prodNo);
}
