package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Cate01;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Cate01Repository extends JpaRepository<Cate01, String> {
    public Cate01 findByCate01No(String cate01No);
}
