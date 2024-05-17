package kr.co.lotteon.repository;

import kr.co.lotteon.entity.PointHistory;
import kr.co.lotteon.entity.UserPoint;
import kr.co.lotteon.repository.custom.UserPointRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, Integer> , UserPointRepositoryCustom {

    // 회원 아이디로 포인트 조회
    public UserPoint findByUserId(String userId);

}
