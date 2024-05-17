package kr.co.lotteon.service;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.CartInfoDTO;
import kr.co.lotteon.entity.CartProduct;
import kr.co.lotteon.entity.ProdOptDetail;
import kr.co.lotteon.entity.Product;
import kr.co.lotteon.entity.Productimg;
import kr.co.lotteon.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestService {

    private final ProductRepository productRepository;
    private final ProdOptionRepository prodOptionRepository;
    private final ProdOptDetailRepository prodOptDetailRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final WishRepository wishRepository;
    private final ModelMapper modelMapper;

    // 장바구니 조회
    public Map<String, List<CartInfoDTO>> findCartProdNoTest(int cartNo) {

        List<CartProduct> carts = cartProductRepository.findByCartNo(cartNo);
        //log.info("findCartProdNo....1: " + carts.toString());

        if (carts.isEmpty()) {
            log.info("No cart products found for cartNo: " + cartNo);
            return Collections.emptyMap(); // emptyMap로 수정
        }

        // 상품조회를 위해 optNo 와 prodNo 리스트 추출
        List<Integer> optNos = carts.stream()
                .map(CartProduct::getOptNo)
                .toList();
        List<Integer> prodNos = carts.stream()
                .map(CartProduct::getProdNo)
                .toList();

        // prodOptDetail 조회
        List<ProdOptDetail> optProdOptDetails = new ArrayList<>();
        for (int optNo : optNos) {
            ProdOptDetail optProdOptDetail = prodOptDetailRepository.selectOptDetailWihtName(optNo);
            optProdOptDetails.add(optProdOptDetail);
        }
        //log.info("findCartProdNo....2: " + optProdOptDetails.toString());


        List<Product> products = new ArrayList<>();
        for (int prodNo : prodNos) {
            Tuple productTuple = productRepository.selectProductById(prodNo);
            //log.info("selectProductById...:" + productTuple.toString());
            Product product = productTuple.get(0, Product.class);
            Productimg productImg = productTuple.get(1, Productimg.class);
            if (productImg != null) {
                product.setThumb190(productImg.getThumb190());
            }
            products.add(product);
        }
        //log.info("findCartProdNo....3: " + products.toString());

        List<CartInfoDTO> cartInfoDTOs = new ArrayList<>();
        for (int i = 0; i < carts.size(); i++) {
            CartProduct cartProduct = carts.get(i);
            ProdOptDetail optProdOptDetail = optProdOptDetails.get(i);
            Product product = products.get(i);

            // CartInfoDTO 객체 생성 및 설정
            CartInfoDTO cartInfoDTO = new CartInfoDTO();
            cartInfoDTO.setProdNo(product.getProdNo());
            cartInfoDTO.setProdName(product.getProdName());
            cartInfoDTO.setProdInfo(product.getProdInfo());
            cartInfoDTO.setProdDiscount(product.getProdDiscount());
            cartInfoDTO.setProdPrice(product.getProdPrice());
            cartInfoDTO.setProdCompany(product.getProdCompany()); // 회사명 추가
            cartInfoDTO.setProdDeliveryFee(product.getProdDeliveryFee()); // 배송비 추가
            if (product.getThumb190() != null) {
                cartInfoDTO.setThumb190(product.getThumb190());
            }
            if (optProdOptDetail != null) {
                cartInfoDTO.setOptValue1(optProdOptDetail.getOptValue1());
                cartInfoDTO.setOptValue2(optProdOptDetail.getOptValue2());
                cartInfoDTO.setOptValue3(optProdOptDetail.getOptValue3());
                cartInfoDTO.setOptStock(optProdOptDetail.getOptStock());
                cartInfoDTO.setOptPrice(optProdOptDetail.getOptPrice());
            }
            cartInfoDTO.setCount(cartProduct.getCount());
            cartInfoDTO.setCartProdNo(cartProduct.getCartProdNo());

            // CartInfoDTO를 리스트에 추가
            cartInfoDTOs.add(cartInfoDTO);
        }
        // 여기서부터 MAP으로 묶는 코드
        // 상품 List를 회사명으로 묶어서 MAP로 저장
        Map<String, List<CartInfoDTO>> companyMap = new HashMap<>();

        for (CartInfoDTO info : cartInfoDTOs) {
            // 회사명이 이미 Map에 있는지 확인
            // contailnsKey = Map 에 지정된 key가 있는지 확인하는데 사용
            if (companyMap.containsKey(info.getProdCompany())) {

                // 이미 있는 경우, 해당 회사명의 List를 가져와서 DTO를 추가
                List<CartInfoDTO> existingCompany = companyMap.get(info.getProdCompany());
                existingCompany.add(info);

            } else {

                // 없는 경우, 새로운 List를 생성하고 DTO를 추가한 후 Map에 추가
                List<CartInfoDTO> newList = new ArrayList<>();
                newList.add(info);
                companyMap.put(info.getProdCompany(), newList);
            }
        }

        // 결과 출력
        for (Map.Entry<String, List<CartInfoDTO>> entry : companyMap.entrySet()) {
            System.out.println("회사명: " + entry.getKey());
            System.out.println("상품 리스트: " + entry.getValue());
            System.out.println();
        }

        return companyMap;
        /*
            Map<String, List<CartInfoDTO>> companyMap -> Map[ 회사명, List<CartInfoDTO> cartInfoDTOs ]
            
            List<CartInfoDTO> cartInfoDTOs -> List[ 상품정보들 ]
            
         */

    }

}
