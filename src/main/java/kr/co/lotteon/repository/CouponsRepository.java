package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Coupons;
import kr.co.lotteon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CouponsRepository extends JpaRepository<Coupons, String> {
    // 쿠폰 번호로 coupon 테이블에서 쿠폰 정보 조회
    public Coupons findByCpNo(int cpNo);
    

}
