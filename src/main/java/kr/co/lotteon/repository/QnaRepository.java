package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Qna;
import kr.co.lotteon.repository.custom.QnaRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Integer>, QnaRepositoryCustom {

    public int countByUserId(String userId);

    public Page<Qna> findByQnaCate(String cate, Pageable pageable);

    List<Qna> findAllByOrderByQnaDateDesc();
}
