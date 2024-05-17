package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Banner;
import kr.co.lotteon.repository.custom.BannerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer>, BannerRepositoryCustom {

    // 배너 리스트 출력
    public List<Banner> findByBanImgCate(String banImgCate);
}
