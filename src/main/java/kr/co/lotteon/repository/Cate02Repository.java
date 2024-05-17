package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Cate01;
import kr.co.lotteon.entity.Cate02;
import kr.co.lotteon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Cate02Repository extends JpaRepository<Cate02, String> {
    // 대분류 선택에 따른 중분류 조회
    List<Cate02> findByCate01No(String cate01No);
    public Cate02 findByCate02No(String cate02No);

}
