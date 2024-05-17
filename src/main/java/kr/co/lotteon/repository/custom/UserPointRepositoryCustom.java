package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.entity.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPointRepositoryCustom {
    public Page<PointHistory> selectPoints(String userId, PageRequestDTO pageRequestDTO, Pageable pageable);

    List<PointHistory> myHomeSelectPoints(String userId);

    // 관리자 - 회원관리 - 포인트관리 //
    public PageResponseDTO userPointControl(PageRequestDTO pageRequestDTO, Pageable pageable);
}
