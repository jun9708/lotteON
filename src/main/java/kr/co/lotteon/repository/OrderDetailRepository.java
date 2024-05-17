package kr.co.lotteon.repository;

import kr.co.lotteon.entity.OrderDetail;
import kr.co.lotteon.repository.custom.OrderDetailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>, OrderDetailRepositoryCustom {
    public List<OrderDetail>  findByOrderNo(int OrderNo);

}
