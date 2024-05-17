package kr.co.lotteon.repository.admin;

import kr.co.lotteon.entity.Faq;
import kr.co.lotteon.entity.Notice;
import kr.co.lotteon.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminQnaRepository extends JpaRepository<Qna, Integer> {


    // qna 글 리스트 출력 페이징 메서드
    public Page<Qna> findAllByOrderByQnaDateDesc(Pageable pageable);

    // qna 카테고리 + 타입별 조회
    public Page<Qna> findByQnaCateAndQnaTypeOrderByQnaDateDesc(String cate,String type, Pageable pageable);

    // qna 카테고리만 조회
    public Page<Qna> findByQnaCateOrderByQnaDateDesc(String cate, Pageable pageable);
}
