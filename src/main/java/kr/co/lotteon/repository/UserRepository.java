package kr.co.lotteon.repository;

import kr.co.lotteon.entity.User;
import kr.co.lotteon.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    public Optional<User> findByUserHp(String userHp);
    public Optional<User> findByUserEmail(String Email);
    public Optional<User> findByUserEmailAndUserId(String Email, String UserId);

}
