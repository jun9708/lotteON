package kr.co.lotteon.service;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProdOptionRepository prodOptionRepository;
    private final ProdOptDetailRepository prodOptDetailRepository;
    private final CartRepository cartRepository;
    private final PdReviewRepository pdReviewRepository;
    private final ProdQnaRepository prodQnaRepository;
    private final CartProductRepository cartProductRepository;
    private final WishRepository wishRepository;
    private final ModelMapper modelMapper;

    // 인덱스 페이지 상품 조회
    public List<ProductDTO> selectIndexProducts(String sort){
        List<Tuple> tuples = productRepository.selectIndexProducts(sort);

        List<ProductDTO> productDTOs = new ArrayList<>();

        for (Tuple tuple : tuples) {
            Product product = tuple.get(0, Product.class);
            Productimg productImg = tuple.get(1, Productimg.class);
            if (product != null) {
                ProductDTO productDTO = new ProductDTO();
                // Product 정보를 ProductDTO로 매핑
                modelMapper.map(product, productDTO);
                // Productimg 정보를 ProductDTO에 설정
                if (productImg != null) {
                    productDTO.setThumb230(productImg.getThumb230());
                }
                // 리스트에 ProductDTO 추가
                productDTOs.add(productDTO);
            }
        }

        return productDTOs;
    }

    // 카테고리 눌러서 상품 조회
    public ProductPageResponseDTO selectProductsByCate(ProductPageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable("prodNo");
        Page<Tuple> pageProd = productRepository.selectProductsByCate(pageRequestDTO, pageable);
        log.info("selectProdsByCate...."+pageProd.toString());
        List<ProductDTO> products = pageProd.getContent().stream()
                .map(tuple -> {
                    Product product = tuple.get(0, Product.class);
                    Productimg productImg = tuple.get(1, Productimg.class);

                    // Productimg에서 썸네일 정보를 가져와서 ProductDTO에 설정
                    if (productImg != null) {
                        product.setThumb190(productImg.getThumb190());
                    }

                    return modelMapper.map(product, ProductDTO.class);
                })
                .toList();

        int total = (int) pageProd.getTotalElements();

        return ProductPageResponseDTO.builder()
                .productPageRequestDTO(pageRequestDTO)
                .dtoList(products)
                .total(total)
                .build();
    }

    // 검색으로 상품조회
    public ProductPageResponseDTO searchProducts(ProductPageRequestDTO pageRequestDTO){
        Pageable pageable = pageRequestDTO.getPageable("prodNo");
        Page<Tuple> pageProd = productRepository.searchProductsByCateAndKeyWord(pageRequestDTO, pageable);
        List<ProductDTO> products = pageProd.getContent().stream()
                .map(tuple -> {
                    Product product = tuple.get(0, Product.class);
                    Productimg productImg = tuple.get(1, Productimg.class);
                    Seller seller = tuple.get(2, Seller.class);
                    // Productimg에서 썸네일 정보를 가져와서 ProductDTO에 설정
                    if (productImg != null) {
                        product.setThumb190(productImg.getThumb190());
                    }
                    if (seller != null) {
                        product.setSellerGrade(seller.getSellerGrade());
                    }

                    return modelMapper.map(product, ProductDTO.class);
                })
                .toList();

        int total = (int) pageProd.getTotalElements();

        return ProductPageResponseDTO.builder()
                .productPageRequestDTO(pageRequestDTO)
                .dtoList(products)
                .total(total)
                .build();
    }

    // 상품 상세보기 페이지로 이동
    public ProductDTO selectProduct(int prodNo) {
        Tuple tuple = productRepository.selectProduct(prodNo);

        Product product = tuple.get(0, Product.class);
        if (product != null) {
            product.setProdHit(product.getProdHit()+1);
            log.info("productHit....!: "+product.toString());
            product = productRepository.save(product);
            Productimg productImg = tuple.get(1, Productimg.class);
            if (productImg != null) {
                product.setThumb230(productImg.getThumb230());
                product.setThumb456(productImg.getThumb456());
                product.setThumb940(productImg.getThumb940());
            }
        }
        return modelMapper.map(product, ProductDTO.class);
    }

    // 상품 위시리스트 등록
    public List<Wish> insertWish(List<Wish> wishes){
        List<Wish> savedWishes = new ArrayList<>();
        for (Wish wish : wishes) {
            log.info("insertWishAtService....:" + wish.toString());
            Wish savedWish = wishRepository.save(wish);
            savedWishes.add(savedWish);
        }
        return savedWishes;
    }

    // 상품 장바구니 등록을 위한 장바구니 번호 조회
    public int findCartNoByUserId(String userId){
        Optional<Cart> optCart = cartRepository.findCartNoByUserId(userId);

        if (optCart.isPresent()) {
            Cart cart = optCart.get();
            return cart.getCartNo();
        }
        return 0;
    }

    // 상품 장바구니 등록
    public List<CartProduct> insertCart(List<CartProduct> cartProducts){
        List<CartProduct> savedCartProducts = new ArrayList<>();
        for (CartProduct cartProduct : cartProducts) {
            log.info("insertCart....:" + cartProduct.toString());
            CartProduct savedCart = cartProductRepository.save(cartProduct);
            savedCartProducts.add(savedCart);
        }

        return savedCartProducts;
    }


    // 상품 옵션 조회
    public ResponseOptionDTO selectProductOption(int prodNo){
        return prodOptionRepository.selectProductOption(prodNo);
    };

    // 장바구니 조회
    /*
    public List<CartInfoDTO> findCartProdNo(int cartNo) {

        List<CartProduct> carts = cartProductRepository.findByCartNo(cartNo);
        //log.info("findCartProdNo....1: " + carts.toString());

        if (carts.isEmpty()) {
            log.info("No cart products found for cartNo: " + cartNo);
            return Collections.emptyList();
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
        //log.info("findCartProdNo..."+cartInfoDTOs.toString());

        return cartInfoDTOs;


    }

     */

    // 상품 리뷰 조회
    public PageResponseDTO selectProdReviewForView(int prodNo, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("prodNo");
        Page<Tuple> tuplePage = pdReviewRepository.selectProdReviewForView(prodNo, pageable, pageRequestDTO);

        List<PdReviewDTO> pdReviewDTOList = tuplePage.getContent().stream()
                .map(tuple -> {
                    PdReview pdReview = tuple.get(0, PdReview.class);
                    String revThumb = tuple.get(1, String.class);
                    PdReviewDTO pdReviewDTO = modelMapper.map(pdReview, PdReviewDTO.class);
                    pdReviewDTO.setRevThumb(revThumb);
                    return pdReviewDTO;
                }).toList();

        int total = (int) tuplePage.getTotalElements();

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(pdReviewDTOList)
                .total(total)
                .build();
    }

    // 상품 문의 조회
    public PageResponseDTO selectProdQna(int prodNo, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("prodQnaNo");
        return prodQnaRepository.selectProdQna(prodNo, pageable, pageRequestDTO);
    }

    // 상품 문의 글 작성
    public int writeProdQna(ProdQnaDTO prodQnaDTO) {
        ProdQna prodQna = modelMapper.map(prodQnaDTO, ProdQna.class);
        ProdQna saveProdQna = prodQnaRepository.save(prodQna);
        if (saveProdQna.getProdQnaTitle().equals(prodQnaDTO.getProdQnaTitle())) {
            return 1;
        } else {
            return 0;
        }
    }
}