package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Integer> {

    public List<Faq> findByFaqCate(String cate);
}
