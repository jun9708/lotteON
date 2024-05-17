package kr.co.lotteon.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import kr.co.lotteon.entity.QFaq;
import kr.co.lotteon.repository.custom.AdminFaqRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AdminFaqRepositoryImpl implements AdminFaqRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private final QFaq qFaq = QFaq.faq;

    // delete * from `faq` where faqNo = ?;
    @Transactional
    @Override
    public boolean deleteAdminCs(int[] faq_faqNos) {
        try{

            for(int i=0; i<faq_faqNos.length; i++){
                long result = jpaQueryFactory
                        .delete(qFaq)
                        .where(qFaq.faqNo.eq(faq_faqNos[i]))
                        .execute();
                // update 실패시 false 반환
                if(result == 0){
                    return false;
                }
            }

            // for문의 update 모두 성공하면 true 반환
            return true;
        } catch(Exception e){
            return false;
        }
    }

}
