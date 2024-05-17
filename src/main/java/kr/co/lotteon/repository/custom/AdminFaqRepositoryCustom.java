package kr.co.lotteon.repository.custom;

import org.springframework.stereotype.Repository;

public interface AdminFaqRepositoryCustom {

    // admin cs faq 게시글 선택 삭제
    public boolean deleteAdminCs(int[] faq_faqNos);

}
