package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.ResponseOptionDTO;
import kr.co.lotteon.entity.ProdOptDetail;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProdOptDetailRepositoryCustom {

    // 옵션 디테일, 옵션 이름 조회
    public ProdOptDetail selectOptDetailWihtName(int optNo);
}
