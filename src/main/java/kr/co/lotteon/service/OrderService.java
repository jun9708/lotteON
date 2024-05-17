package kr.co.lotteon.service;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.CartInfoDTO;
import kr.co.lotteon.dto.OrderInfoDTO;
import kr.co.lotteon.dto.UserDTO;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final CartProductRepository cartProductRepository;
    private final ProdOptDetailRepository prodOptDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderdetailRepository;
    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    // 장바구니 조회
    public Map<String, List<CartInfoDTO>> findCartProdNo(int cartProdNo) {

        Optional<CartProduct> carts = cartProductRepository.findById(cartProdNo);

        int optNo = carts.get().getOptNo();
        int prodNo = carts.get().getProdNo();
        //log.info("findCartProdNo....3: " + prodNo);

        ProdOptDetail optProdOptDetail = prodOptDetailRepository.selectOptDetailWihtName(optNo);
        //log.info("findCartProdNo....4: " + optProdOptDetail);

        Product product = null;
        try {
            Tuple productTuple = productRepository.selectProductById(prodNo);
            //log.info("findCartProdNo....5: " + productTuple);

            if (productTuple != null) {
                product = productTuple.get(0, Product.class);
                Productimg productImg = productTuple.get(1, Productimg.class);
                if (productImg != null) {
                    product.setThumb190(productImg.getThumb190());
                }
                //log.info("findCartProdNo....6: " + product);

            }else {
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }

        List<CartInfoDTO> cartInfoDTOs = new ArrayList<>();
        try {
            CartInfoDTO cartInfoDTO = new CartInfoDTO();
            cartInfoDTO.setProdNo(product.getProdNo());
            cartInfoDTO.setProdName(product.getProdName());
            cartInfoDTO.setProdInfo(product.getProdInfo());
            cartInfoDTO.setProdDiscount(product.getProdDiscount());
            cartInfoDTO.setProdPrice(product.getProdPrice());
            cartInfoDTO.setProdCompany(product.getProdCompany()); // 회사명 추가
            cartInfoDTO.setProdSeller(product.getProdSeller()); // 회사명 추가
            cartInfoDTO.setProdDeliveryFee(product.getProdDeliveryFee()); // 배송비 추가
            if (product.getThumb190() != null) {
                cartInfoDTO.setThumb190(product.getThumb190());
            }
            if (optProdOptDetail != null) {
                cartInfoDTO.setOptValue1(optProdOptDetail.getOptValue1());
                cartInfoDTO.setOptValue2(optProdOptDetail.getOptValue2());
                cartInfoDTO.setOptValue3(optProdOptDetail.getOptValue3());
                cartInfoDTO.setOptStock(optProdOptDetail.getOptStock());
                cartInfoDTO.setOptDetailNo(optProdOptDetail.getOptDetailNo());
                cartInfoDTO.setOptPrice(optProdOptDetail.getOptPrice());
            }
            cartInfoDTO.setCount(carts.get().getCount());
            cartInfoDTO.setCartProdNo(carts.get().getCartProdNo());

            cartInfoDTOs.add(cartInfoDTO);
            log.info("product...:" + cartInfoDTOs);

        }catch (Exception e){
            log.info(e.getMessage());
        }


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
    }

    public Map<String, List<CartInfoDTO>> orderDirect(List<CartInfoDTO> cartInfoDTOS){

        Map<String, List<CartInfoDTO>> companyMap = new HashMap<>();
        List<CartInfoDTO> list = new ArrayList<>();

        for (CartInfoDTO cartInfo: cartInfoDTOS){

            int optNo = cartInfo.getOptDetailNo();
            int prodNo = cartInfo.getProdNo();

            ProdOptDetail optProdOptDetail = prodOptDetailRepository.selectOptDetailWihtName(optNo);
            log.info("findCartProdNo....4: " + optProdOptDetail);
            Product product = null;

            try {
                Tuple productTuple = productRepository.selectProductById(prodNo);
                log.info("findCartProdNo....5: " + productTuple);

                if (productTuple != null) {
                    product = productTuple.get(0, Product.class);
                    Productimg productImg = productTuple.get(1, Productimg.class);
                    if (productImg != null) {
                        product.setThumb190(productImg.getThumb190());
                    }
                    log.info("findCartProdNo....6: " + product);

                }
                try {
                    CartInfoDTO cartInfoDTO = new CartInfoDTO();
                    cartInfoDTO.setProdNo(product.getProdNo());
                    cartInfoDTO.setProdName(product.getProdName());
                    cartInfoDTO.setProdInfo(product.getProdInfo());
                    cartInfoDTO.setProdDiscount(product.getProdDiscount());
                    cartInfoDTO.setProdPrice(product.getProdPrice());
                    cartInfoDTO.setProdCompany(product.getProdCompany()); // 회사명 추가
                    cartInfoDTO.setProdSeller(product.getProdSeller()); // 회사명 추가
                    cartInfoDTO.setProdDeliveryFee(product.getProdDeliveryFee()); // 배송비 추가
                    if (product.getThumb190() != null) {
                        cartInfoDTO.setThumb190(product.getThumb190());
                    }
                    if (optProdOptDetail != null) {
                        cartInfoDTO.setOptValue1(optProdOptDetail.getOptValue1());
                        cartInfoDTO.setOptValue2(optProdOptDetail.getOptValue2());
                        cartInfoDTO.setOptValue3(optProdOptDetail.getOptValue3());
                        cartInfoDTO.setOptStock(optProdOptDetail.getOptStock());
                        cartInfoDTO.setOptDetailNo(optProdOptDetail.getOptDetailNo());
                        cartInfoDTO.setOptPrice(optProdOptDetail.getOptPrice());
                    }
                    cartInfoDTO.setCount(cartInfo.getCount());
                    list.add(cartInfoDTO);
                    log.info("findCart....7:" + list);
                } catch (Exception e) {
                    log.info(e.getMessage());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }



        for (CartInfoDTO info : list) {
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


        return  companyMap;
    }


    public UserDTO selectUser(String userId) {
        Tuple userTuple = userRepository.selectUserInfoWithPoint(userId);

        User user = userTuple.get(0, User.class);
        if (user != null) {
            UserPoint userPoint = userTuple.get(1, UserPoint.class);
            if (userPoint != null) {
                user.setPointNo(userPoint.getPointNo());
                user.setPointBalance(userPoint.getPointBalance());
            }
        }

        log.info("here...!!! : " + userTuple);
        return  modelMapper.map(user, UserDTO.class);
    }

    // 결제버튼 눌렀을 시 처리과정
    public int insertOrders(OrderInfoDTO orderInfoDTO){
        Map<String, String> orders = orderInfoDTO.getOrders();
        String orderPay = orders.get("orderPay");
        if ("type4".equals(orderPay)){
            orders.put("orderStatus", "입금대기");
        }else {
            orders.put("orderStatus", "배송준비");
        }

        Orders ordered = saveOrders(orders);
        int orderNo = ordered.getOrderNo();
        int orderPrice = ordered.getOrderPrice();
        int userUsedPoint = ordered.getUserUsedPoint();
        String userId = ordered.getUserId();

        log.info("Saved order: {}", ordered);

        List<Map<String, String>> orderDetails = orderInfoDTO.getOrderDetails();

        for (Map<String, String> orderDetail : orderDetails) {
            int detailPrice = Integer.parseInt(orderDetail.get("detailPrice"));
            int detailPoint = (int) Math.round(userUsedPoint * ((double) detailPrice / orderPrice));
            if ("type4".equals(orderPay)){
                orderDetail.put("detailStatus", "입금대기");
            }else {
                orderDetail.put("detailStatus", "배송준비");
            }

            orderDetail.put("detailPoint", String.valueOf(detailPoint));
            saveOrderDetail(orderNo, orderDetail);

            int cartProdNo = Integer.parseInt(orderDetail.get("cartProdNo"));
            // cart 테이블에서 삭제
            if (cartProdNo != 0){
                cartProductRepository.deleteById(cartProdNo);
            }

            int prodNo = Integer.parseInt(orderDetail.get("prodNo"));
            int optNo = Integer.parseInt(orderDetail.get("optNo"));
            int count = Integer.parseInt(orderDetail.get("count"));

            if (optNo == 0) {
                // product 테이블의 재고 감소 및 판매된 수량 증가
                decreaseProductStock(prodNo, count);
            } else {
                // prodOptDetail 테이블의 옵션 재고 감소
                decreaseProdOptStock(optNo, count);
            }

        }
        if (userUsedPoint != 0){
            selectUserPoint(userId, userUsedPoint);
        }
        return orderNo;

    }
    
    // orders 테이블에 저장
    public Orders saveOrders(Map<String, String> orderData) {
        Orders orders = new Orders();
        orders.setUserId(orderData.get("userId"));
        orders.setOrderReceiver(orderData.get("orderReceiver"));
        orders.setOrderHp(orderData.get("orderHp"));
        orders.setOrderAddr(orderData.get("orderAddr"));
        orders.setOrderPay(orderData.get("orderPay"));
        orders.setOrderPrice(Integer.parseInt(orderData.get("orderPrice")));
        String userUsedPointStr = orderData.get("userUsedPoint");
        int userUsedPoint = userUsedPointStr != null && !userUsedPointStr.isEmpty() ? Integer.parseInt(userUsedPointStr) : 0;
        orders.setUserUsedPoint(userUsedPoint);
        orders.setOrderMemo(orderData.get("orderMemo"));
        orders.setOrderStatus(orderData.get("orderStatus"));

        return ordersRepository.save(orders);
    }

    //orderDetail 테이블에 저장
    public void saveOrderDetail(int orderNo, Map<String, String> orderDetail) {

        int prodNo = Integer.parseInt(orderDetail.get("prodNo"));
        int optNo = Integer.parseInt(orderDetail.get("optNo"));
        int count = Integer.parseInt(orderDetail.get("count"));
        int detailPrice = Integer.parseInt(orderDetail.get("detailPrice"));
        int detailPoint = Integer.parseInt(orderDetail.get("detailPoint"));
        String prodSeller = orderDetail.get("prodSeller");
        String detailStatus = orderDetail.get("detailStatus");

        OrderDetail detail = new OrderDetail();
        detail.setOrderNo(orderNo);
        detail.setProdNo(prodNo);
        detail.setOptNo(optNo);
        detail.setCount(count);
        detail.setDetailPrice(detailPrice);
        detail.setDetailPoint(detailPoint);
        detail.setProdSeller(prodSeller);
        detail.setDetailStatus(detailStatus);

        log.info("detail: {}", detail);
        orderdetailRepository.save(detail);
    }

    //userPoint 테이블 금액 차감
    public void selectUserPoint(String userId, int userUsedPoint){
        UserPoint userPoint = userPointRepository.findByUserId(userId);
        log.info("userPoint: " + userPoint);

        if (userPoint != null) {
            userPoint.setPointNo(userPoint.getPointNo());
            userPoint.setPointBalance(userPoint.getPointBalance()-userUsedPoint);
            userPointRepository.save(userPoint);
            if (userUsedPoint != 0){
                savePointHistory(userPoint.getPointNo(), userUsedPoint);
            }
        }
    }

    // 포인트 히스토리 저장
    private void savePointHistory(int pointNo, int changePoint) {
        // 포인트 내역 엔터티 생성
        PointHistory pointHistory = new PointHistory();
        pointHistory.setPointNo(pointNo);
        pointHistory.setChangePoint(changePoint);
        pointHistory.setChangeCode("상품구매");
        pointHistory.setChangeType("사용");
        log.info("pointHistory: {}", pointHistory);
        // 포인트 내역 저장
        pointHistoryRepository.save(pointHistory);
    }

    // product 테이블의 재고(prodStock) 감소 및 판매된 수량(prodSold) 증가
    private void decreaseProductStock(int prodNo, int count) {
        // 해당 상품의 정보 가져오기
        Optional<Product> productOptional = productRepository.findById(prodNo);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // 재고 감소
            int newStock = product.getProdStock() - count;
            product.setProdStock(newStock);

            // 판매된 수량 증가
            int newSold = product.getProdSold() + count;
            product.setProdSold(newSold);

            // 저장
            productRepository.save(product);
        }

    }

    // prodOptDetail 테이블의 옵션 재고 감소
    private void decreaseProdOptStock(int optDetailNo, int count) {
        // 해당 옵션의 정보 가져오기
        Optional<ProdOptDetail> optProdOptDetail = prodOptDetailRepository.findById(optDetailNo);
        if (optProdOptDetail.isPresent()) {
            ProdOptDetail prodOptDetail = optProdOptDetail.get();
            // 재고 감소
            int newStock = prodOptDetail.getOptStock() - count;
            prodOptDetail.setOptStock(newStock);

            // 저장
            prodOptDetailRepository.save(prodOptDetail);

            int prodNo = prodOptDetail.getProdNo();
            Optional<Product> productOptional = productRepository.findById(prodNo);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                // 판매된 수량 증가
                int newSold = product.getProdSold() + count;
                product.setProdSold(newSold);
                // 상품 정보 저장
                productRepository.save(product);
            }
        }
    }

    public Orders orderComplete(int orderNo) {
        Optional<Orders> optOrders = ordersRepository.findById(orderNo);
        Orders orders = new Orders();
        if (optOrders.isPresent()) {
            orders = optOrders.get();
        }
        String userId = orders.getUserId();

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String userName = user.getUserName();
            String userHp = user.getUserHp();

            orders.setUserName(userName);
            orders.setUserHp(userHp);
        }

        return orders;

    }

    public List<CartInfoDTO> orderDetailComplete(int orderNo) {
        List<OrderDetail> orderDetailList = orderdetailRepository.findByOrderNo(orderNo);
        List<CartInfoDTO> cartInfoDTOs = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {

            int optNo = orderDetail.getOptNo();
            int prodNo = orderDetail.getProdNo();

            ProdOptDetail optProdOptDetail = prodOptDetailRepository.selectOptDetailWihtName(optNo);

            Product product = null;
            try {
                Tuple productTuple = productRepository.selectProductById(prodNo);

                if (productTuple != null) {
                    product = productTuple.get(0, Product.class);
                    Productimg productImg = productTuple.get(1, Productimg.class);
                    if (productImg != null) {
                        product.setThumb190(productImg.getThumb190());
                    }

                }else {
                }
            }catch (Exception e){
                log.info(e.getMessage());
            }
            try {
                CartInfoDTO cartInfoDTO = new CartInfoDTO();
                cartInfoDTO.setProdNo(product.getProdNo());
                cartInfoDTO.setProdName(product.getProdName());
                cartInfoDTO.setProdInfo(product.getProdInfo());
                cartInfoDTO.setProdDiscount(product.getProdDiscount());
                cartInfoDTO.setProdPrice(product.getProdPrice());
                cartInfoDTO.setProdDeliveryFee(product.getProdDeliveryFee()); // 배송비 추가
                if (product.getThumb190() != null) {
                    cartInfoDTO.setThumb190(product.getThumb190());
                }
                if (optProdOptDetail != null) {
                    cartInfoDTO.setOptValue1(optProdOptDetail.getOptValue1());
                    cartInfoDTO.setOptValue2(optProdOptDetail.getOptValue2());
                    cartInfoDTO.setOptValue3(optProdOptDetail.getOptValue3());
                    cartInfoDTO.setOptPrice(optProdOptDetail.getOptPrice());
                }
                cartInfoDTO.setCount(orderDetail.getCount());

                cartInfoDTOs.add(cartInfoDTO);

            }catch (Exception e){
                log.info(e.getMessage());
            }

        }

        return cartInfoDTOs;

    }
}
