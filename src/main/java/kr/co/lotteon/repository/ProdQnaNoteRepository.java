package kr.co.lotteon.repository;

import kr.co.lotteon.entity.ProdQnaNote;
import kr.co.lotteon.repository.custom.ProdQnaNoteRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdQnaNoteRepository extends JpaRepository<ProdQnaNote, Integer> {

    ProdQnaNote findByProdQnaNo(int prodQnaNo);


}
