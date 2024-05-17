package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Notice;
import kr.co.lotteon.entity.ProdQna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    // 공지사항 글 리스트 출력 페이징 메서드
    public Page<Notice> findAll(Pageable pageable);
    public Page<Notice> findByNoticeCateOrderByNoticeDateDesc(String cate,Pageable pageable);

    // 관리자 메인페이지 - 공지사항 최신순 5개 조회
    // 판매자 메인페이지 - 공지사항 최신순 5개 조회
    public List<Notice> findTop5ByOrderByNoticeDateDesc();

    List<Notice> findAllByOrderByNoticeDateDesc();

    Page<Notice> findAllByOrderByNoticeDateDesc(Pageable pageable);
}