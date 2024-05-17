package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Cate01;
import kr.co.lotteon.entity.Cate03;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Cate03Repository extends JpaRepository<Cate03, String> {
    public Cate03 findByCate03No(String cate03No);
}
