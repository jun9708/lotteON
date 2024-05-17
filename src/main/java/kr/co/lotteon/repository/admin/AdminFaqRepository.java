package kr.co.lotteon.repository.admin;

import kr.co.lotteon.entity.Faq;
import kr.co.lotteon.repository.custom.AdminFaqRepositoryCustom;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminFaqRepository extends JpaRepository<Faq, Integer>, AdminFaqRepositoryCustom {

    // 자주묻는질문 글 리스트 출력 페이징 메서드
    public Page<Faq> findAllByOrderByFaqDateDesc(Pageable pageable);

    // 공지사항 카테고리 + 타입별 조회
    public Page<Faq> findByFaqCateAndFaqTypeOrderByFaqDateDesc(String cate,String type, Pageable pageable);

    // 공지사항 카테고리만 조회
    public Page<Faq> findByFaqCateOrderByFaqDateDesc(String cate, Pageable pageable);

    // 글 조회수 업
    @Modifying
    @Query("UPDATE Faq f Set f.faqHit = f.faqHit + 1 WHERE f.faqNo = :faqNo")
    void incrementHitByFaqNo(@Param("faqNo") int faqNo);
}
