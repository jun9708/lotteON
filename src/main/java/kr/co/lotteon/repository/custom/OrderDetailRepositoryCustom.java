package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.OrderDetailDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderDetailRepositoryCustom {
    public Map<String, List<?>> selectDetailCheck(int orderNo, OrderDetailDTO dto);
}
