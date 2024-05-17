package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Cart;
import kr.co.lotteon.entity.Cate01;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    public Optional<Cart> findCartNoByUserId(String userId);

    public Cart findByUserId(String userId);
}
