package kr.co.lotteon.repository;

import kr.co.lotteon.entity.Seller;
import kr.co.lotteon.repository.custom.SellerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, String>, SellerRepositoryCustom {
    // seller 정보를 security에 저장하기 위한 JPA
    public Optional<Seller> findByUserId(String userId);

    public Optional<Seller> findBySellerName(String sellerName);
    public Optional<Seller> findByCompany(String company);
    public Optional<Seller> findByLicenseNum(String licenseNum );
    public Optional<Seller> findByBusinessNum(String businessNum);
    public Optional<Seller> findBySellerHp(String sellerHp);

    public Optional<Seller> findByFax(String fax);
}
