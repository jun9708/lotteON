package kr.co.lotteon.repository.admin;

import kr.co.lotteon.entity.Notice;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminNoticeRepository extends JpaRepository<Notice, Integer> {

    // 공지사항 글 리스트 출력 페이징 메서드
    public Page<Notice> findAllByOrderByNoticeDateDesc(Pageable pageable);

    // 공지사항 카테고리별 조회
    public Page<Notice> findByNoticeCateOrderByNoticeDateDesc(String cate, Pageable pageable);

    // 공지사항 카테+타입 조회
    public Page<Notice> findByNoticeCateAndNoticeTypeOrderByNoticeDateDesc(String cate,String type,Pageable pageable);

    // 글 조회수 업
    @Modifying
    @Query("UPDATE Notice n SET n.noticeHit = n.noticeHit + 1 WHERE n.noticeNo = :noticeNo")
    void incrementHitByNoticeNo(@Param("noticeNo") int noticeNo);

}
