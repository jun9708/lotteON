package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Orders;
import kr.co.lotteon.repository.custom.OrdersRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer>, OrdersRepositoryCustom {
    public List<Orders> findByUserId(String UserId);
    public long countByUserId(String userId);
}
