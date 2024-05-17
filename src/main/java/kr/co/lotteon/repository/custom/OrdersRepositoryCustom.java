package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.MyOrderPageRequestDTO;
import kr.co.lotteon.dto.MyOrderPageResponseDTO;
import kr.co.lotteon.dto.OrderDetailDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

@Repository
public interface OrdersRepositoryCustom {
    public MyOrderPageResponseDTO selectMyOrdersByDate(String userId,Pageable pageable,MyOrderPageRequestDTO myOrderPageRequestDTO);

    LinkedHashMap<Integer, List<OrderDetailDTO>> selectMyOrdersHome(String userId);

}
