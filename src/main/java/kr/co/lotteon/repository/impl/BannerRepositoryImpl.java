package kr.co.lotteon.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.Banner;
import kr.co.lotteon.entity.QBanner;
import kr.co.lotteon.repository.custom.BannerRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private final QBanner qBanner = QBanner.banner;
    //배너 선택삭제
    @Transactional
    public boolean deleteBanner(int[] banNo) {
        try{
            for(int i=0; i<banNo.length; i++){
                long result = jpaQueryFactory
                        .delete(qBanner)
                        .where(qBanner.banNo.eq(banNo[i]))
                        .execute();
                if(result == 0){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            log.error("err:" + e.getMessage());
        }
        return false;
    }
    //배너 불러오기
    public List<Banner> selectBanners(String banImgCate){

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        return jpaQueryFactory
                .selectFrom(qBanner)
                .where(qBanner.banUsable.eq("YES"))
                .where(qBanner.banSdate.loe(currentDate))
                .where(qBanner.banEdate.goe(currentDate))
                .where(
                        //배너 시작날짜가 현재날짜와 같고 배너 시작시간이 현재시간보다 전일때
                        qBanner.banSdate.eq(currentDate).and(qBanner.banStime.before(currentTime))
                                .or(
                                        //또는 배너시작날짜가 현재날짜보다 전일때 그리고 배너 끝나는 날짜가 현재날짜보다 후일때
                                        qBanner.banSdate.before(currentDate)
                                                .and(qBanner.banEdate.after(currentDate))
                                )
                                .or(
                                        //또는 배너 끝나는 날짜가 현재날짜와 동일할때 그리고 배너 끝나는 시간이 현재시간보다 후일때
                                        qBanner.banEdate.eq(currentDate).and(qBanner.banEtime.after(currentTime))
                                )
                )
                .where(qBanner.banImgCate.eq(banImgCate))
                .fetch();
    }

    //배너 활성화
    public List<Banner> selectBannerUsable(String banImgCate) {
        return jpaQueryFactory
                .selectFrom(qBanner)
                .where(qBanner.banImgCate.eq(banImgCate))
                .fetch();
    }
    //메인 슬라이더 배너 활성화
    @Transactional
    public long updateBannerUsable(int banNo, String banUsable) {
        try{
            long result = jpaQueryFactory
                    .update(qBanner)
                    .set(qBanner.banUsable, banUsable)
                    .where(qBanner.banNo.eq(banNo))
                    .execute();

            return result;
        }catch(Exception e){
            log.error("error msg :" + e.getMessage());
            return -1;
        }
    }
}
