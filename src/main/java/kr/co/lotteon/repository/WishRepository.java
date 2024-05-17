package kr.co.lotteon.repository;

import kr.co.lotteon.entity.User;
import kr.co.lotteon.entity.Wish;
import kr.co.lotteon.repository.custom.UserRepositoryCustom;
import kr.co.lotteon.repository.custom.WishRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WishRepository extends JpaRepository<Wish, Integer>, WishRepositoryCustom {

}
