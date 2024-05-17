package kr.co.lotteon.service;

import com.querydsl.core.Tuple;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.*;
import kr.co.lotteon.repository.impl.ProductRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyService {

    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderdetailRepository;
    private final WishRepository wishRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;

    private final PdReviewRepository pdReviewRepository;
    private final PdReviewImgRepository pdReviewImgRepository;
    private final PasswordEncoder passwordEncoder;

    private final CouponsRepository couponsRepository;
    private final ModelMapper modelMapper;
    private final UserPointRepository userPointRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final QnaRepository qnaRepository;
    private final ProdQnaRepository prodQnaRepository;
    private final CompanyService companyService;
    private final ProdQnaNoteRepository prodQnaNoteRepository;

    public void selectMyInfo(HttpSession session, String userId){
        log.info("countOrder : " +userId);
        //주문 배송 확인 (개수)
        long countOrder = ordersRepository.countByUserId(userId);
        log.info("countOrder : " +countOrder);

        //할인 쿠폰 (개수)
        String couponStatus = "사용 가능";
        int couponCount = userCouponRepository.countByUserIdAndUcpStatus(userId, couponStatus);
        log.info("couponCount : " +couponCount);

        //포인트 (총합)
        UserPoint userPointsList = userPointRepository.findByUserId(userId);
        int pointBalance = userPointsList.getPointBalance();
        log.info("pointBalance : " +pointBalance);

        //문의내역 (검토중)
        int qnaCount = qnaRepository.countByUserId(userId);
        int prodQndCount = prodQnaRepository.countByUserId(userId);
        int myQnaCount = qnaCount + prodQndCount;

        log.info("qnaCount : " +qnaCount);
        log.info("prodQndCount : " +prodQndCount);


        session.setAttribute("countOrder", countOrder);
        session.setAttribute("couponCount", couponCount);
        session.setAttribute("pointBalance", pointBalance);
        session.setAttribute("myQnaCount", myQnaCount);

    }

    /*
        마이페이지 출력을 위한 service
         - user_id로 user테이블 조회 후 userDTO 반환

    public int myServiceCheck(HttpSession session, String type, String value){
        int result = 0;

        if(type.equals("userEmail"))
    }*/

    public Map<String, Object> selectUserInfo(String userId){
        // 유저테이블에서 아이디로 정보 검색
        Optional<User> optUser = userRepository.findById(userId);
        SellerDTO sellerDTO = new SellerDTO();
        UserDTO userDTO = new UserDTO();
        if(optUser.isPresent()){
            if(optUser.get().getUserRole().equals("SELLER")){
                // 사용자가 seller인 경우
                Optional<Seller> optSeller = sellerRepository.findByUserId(userId);
                sellerDTO = modelMapper.map(optSeller.get(), SellerDTO.class);
            }
            userDTO = modelMapper.map(optUser.get(), UserDTO.class);
        }
        // 그냥 user인 경우

        Map<String, Object> result = new HashMap<>();
        result.put("user", userDTO);
        result.put("seller", sellerDTO);

        return result;
    };
    public UserDTO selectSellerInfo(String sellerId) {
        Optional<User> seller = userRepository.findById(sellerId);
        log.info("seller :" + seller);
        if (seller.isPresent()) {
            return modelMapper.map(seller.get(),UserDTO.class);
        }
        return null;
    }
    // 마이페이지 - 판매자 이름 수정
    public ResponseEntity<?> myInfoUpdateSellerName(String userId, String sellerName){
        long result = sellerRepository.updateSellerNameByUserId(userId,sellerName);
        log.info("userId :" + userId);
        log.info("sellerName :" + sellerName);

        if(result > 0){
            //업데이트가 됐을경우
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }else{
            // 업데이트 실패
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("문제가 발생했습니다.");
        }
    }
    // 마이페이지 - 판매자 연락처 수정
    public ResponseEntity<?> myInfoUpdateSellerHp(String userId, String sellerHp){
        long result = sellerRepository.updateSellerHpByUserId(userId,sellerHp);
        log.info("userId :" + userId);
        log.info("sellerHp :" + sellerHp);

        if(result > 0){
            //업데이트가 됐을경우
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }else{
            // 업데이트 실패
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("문제가 발생했습니다.");
        }
    }
    // 마이페이지 - 판매자 팩스번호 수정
    public ResponseEntity<?> myInfoUpdateFax(String userId, String fax){
        Optional<Seller> optUser = sellerRepository.findByUserId(userId);
        log.info("userId :" + userId);
        log.info("fax :" + fax);
        Map<String,String> result = new HashMap<>();
        if(optUser.isPresent()){
            optUser.get().setFax(fax);
            Seller saveSeller = sellerRepository.save(optUser.get());
            log.info("saveSeller :" + saveSeller);
            if(saveSeller.getFax().equals(fax)){
                result.put("status", "ok");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }else{
                result.put("status", "fail");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        }else{
            result.put("status","notfound");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    // 마이페이지 - 연락처 수정
    public ResponseEntity<?> myInfoUpdateHp(String userId, String userHp){
        Optional<User> optUser = userRepository.findById(userId);
        Map<String, String> result = new HashMap<>();
        if (optUser.isPresent()) {
            optUser.get().setUserHp(userHp);
            User saveUser = userRepository.save(optUser.get());
            if (saveUser.getUserHp().equals(userHp)){
                result.put("status", "ok");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }else{
                result.put("status", "fail");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        }else {
            result.put("status", "notfound");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    // 마이페이지 - 이메일 수정
    public ResponseEntity<?> myInfoUpdateEmail(String userId, String userEmail){
        Optional<User> optUser = userRepository.findById(userId);
        Map<String, String> result = new HashMap<>();
        if (optUser.isPresent()) {
            optUser.get().setUserEmail(userEmail);
            User saveUser = userRepository.save(optUser.get());
            if (saveUser.getUserEmail().equals(userEmail)){
                result.put("status", "ok");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }else{
                result.put("status", "fail");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("result");
            }
        }else {
            result.put("status", "notfound");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("notfound");
        }
    }

    // 마이페이지 - 비밀번호 수정
    public ResponseEntity<?> myInfoUpdatePw(String userId, String userPw){
        Optional<User> optUser = userRepository.findById(userId);
        Map<String, String> result = new HashMap<>();
        if (optUser.isPresent()) {
            optUser.get().setUserPw(userPw);
            User saveUser = userRepository.save(optUser.get());
            if (saveUser.getUserPw().equals(userPw)){
                result.put("status", "ok");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }else{
                result.put("status", "fail");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("result");
            }
        }else {
            result.put("status", "notfound");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("notfound");
        }
    }

    //마이페이지 주소 수정
    public ResponseEntity<?> myInfoUpdateAddr(UserDTO userDTO) {
        log.info("myInfoUpdateAddr :" + userDTO);

        Optional<User> findId = userRepository.findById(userDTO.getUserId());

        if (findId.isPresent()) {

            User user = findId.get();

            user.setUserZip(userDTO.getUserZip());
            user.setUserAddr1(userDTO.getUserAddr1());
            user.setUserAddr2(userDTO.getUserAddr2());
            User savedUser = userRepository.save(user);

            Map<String, User> result = new HashMap<>();
            result.put("data", savedUser);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(result);
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("not found");
        }
    }

    // 마이페이지 - 회원 탈퇴
    public ResponseEntity<?> myInfoUpdateRole(String userId, String userRole){
        Optional<User> optUser = userRepository.findById(userId);
        Map<String, String> result = new HashMap<>();
        if(optUser.isPresent()){
            optUser.get().setUserRole("DELETE");
            User savedUser = userRepository.save(optUser.get());
            if(savedUser.getUserRole().equals("DELETE")){
                result.put("status", "ok");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }else{
                result.put("status","fail");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("result");
            }
        }else{
            result.put("status", "not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }
    }

    // 마이페이지 - 주문내역 조회
    public MyOrderPageResponseDTO selectOrders(String UserId, MyOrderPageRequestDTO myOrderPageRequestDTO) {
        //페이징 처리
        Pageable pageable = myOrderPageRequestDTO.getPageable("no");

        // userId로 Orders 조회
        return ordersRepository.selectMyOrdersByDate(UserId, pageable, myOrderPageRequestDTO);
    }



    // 마이페이지 - 쿠폰 조회
    public PageResponseDTO selectCoupons(PageRequestDTO pageRequestDTO, String userId){

        Pageable pageable = pageRequestDTO.getPageable("cpNo");

        // userId로 userCoupon 조회
        Page<UserCoupon> pageUserCoupon = userCouponRepository.findByUserId(userId, pageable);

        List<UserCoupon> selectUserCoupon = pageUserCoupon.getContent();

        int total = (int) pageUserCoupon.getTotalElements();

        log.info("selectUserCoupon : " + selectUserCoupon);

        // userCoupon에서 조회한 cpNo로 쿠폰 정보 조회
        List<UserCouponDTO> haveCoupons = new ArrayList<>();

        log.info("방금 생성한 haveCoupons : " + haveCoupons);

        if (selectUserCoupon !=null && selectUserCoupon.size()>0){
            for (UserCoupon eachUserCoupon : selectUserCoupon){
                log.info("for문 시작");
                // 1. eachUserCoupon 에서 cpNo 꺼내서 쿠폰 정보 조회
                Coupons findCoupon = couponsRepository.findByCpNo(eachUserCoupon.getCpNo());
                log.info("findCoupon 1 : " + findCoupon);
                // 2. 엔티티를 DTO로 변환
                UserCouponDTO userCouponDTO = modelMapper.map(eachUserCoupon, UserCouponDTO.class);
                couponsDTO couponsDTO = modelMapper.map(findCoupon, couponsDTO.class);
                log.info("userCouponDTO 2 : " + userCouponDTO);
                log.info("couponsDTO 3 : " + couponsDTO);

                // 3. UserCouponDTO 과 couponsDTO 합치기
                userCouponDTO.setCouponsDTO(couponsDTO);
                haveCoupons.add(userCouponDTO);
                log.info("userCouponDTO 4 : " + userCouponDTO);
                log.info("haveCoupons 5 : " + haveCoupons);
                log.info("for문 끝");
            }
        }
        log.info("for문 이후의 haveCoupons : " + haveCoupons);

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(haveCoupons)
                .total(total)
                .build();
    }

    // 마이페이지 - 포인트내역 조회
    public PageResponseDTO selectPoints(String userId, PageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable("pointHisNo");

        Page<PointHistory> pagePointHistory = userPointRepository.selectPoints(userId, pageRequestDTO, pageable);

        List<PointHistoryDTO> dtoList = pagePointHistory.getContent().stream()
                .map(history -> {
                    PointHistoryDTO pointHistoryDTO = new PointHistoryDTO();
                    pointHistoryDTO.setPointNo(history.getPointHisNo());
                    pointHistoryDTO.setPointNo(history.getPointNo());
                    pointHistoryDTO.setChangePoint(history.getChangePoint());
                    pointHistoryDTO.setChangeDate(history.getChangeDate());
                    pointHistoryDTO.setChangeCode(history.getChangeCode());
                    pointHistoryDTO.setChangeType(history.getChangeType());

                    return pointHistoryDTO;
                    // for (String aa : aaaa) {}
                })
                .toList();
        int total = (int) pagePointHistory.getTotalElements();
        log.info("total : " + total);

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    //myHome 포인트내역
    public List<PointHistoryDTO> myHomeselectPoints (String userId){

        List<PointHistory> myPointHistory = userPointRepository.myHomeSelectPoints(userId);
        List<PointHistoryDTO> myPointHistoryDTO = myPointHistory.stream()
                .map(pointHistory -> {
                    PointHistoryDTO pointHistoryDTO = new PointHistoryDTO();
                    pointHistoryDTO.setPointNo(pointHistory.getPointHisNo());
                    pointHistoryDTO.setPointNo(pointHistory.getPointNo());
                    pointHistoryDTO.setChangePoint(pointHistory.getChangePoint());
                    pointHistoryDTO.setChangeDate(pointHistory.getChangeDate());
                    pointHistoryDTO.setChangeCode(pointHistory.getChangeCode());
                    pointHistoryDTO.setChangeType(pointHistory.getChangeType());

                    return pointHistoryDTO;
                })
                .toList();

        log.info("myHomeselectPoints : " + myPointHistoryDTO);

        return myPointHistoryDTO;
    }

    //myHome 주문내역
    public  LinkedHashMap<Integer, List<OrderDetailDTO>> myHomeSelectOrder (String userId){

        return ordersRepository.selectMyOrdersHome(userId);

    }

    // my-review 작성
    @Value("${file.upload.path}")
    private String fileUploadPath;
    public String writeReview(PdReviewDTO pdReviewDTO, MultipartFile revImage){
        log.info("pdReviewDTO : " + pdReviewDTO);
        // 1. 이미지 저장
        String path = new File(fileUploadPath).getAbsolutePath();

        String sName = null;
        if(!revImage.isEmpty()){
            String oName = revImage.getOriginalFilename();

            String ext = oName.substring(oName.lastIndexOf("."));

            sName = UUID.randomUUID().toString()+ext;

            try{
                Thumbnails.of(revImage.getInputStream())
                        .size(100, 100)
                        .toFile(new File(path, "review"+sName));

                //2. 리뷰정보 DB저장
                //review 저장
                PdReview pdReview = modelMapper.map(pdReviewDTO, PdReview.class);

                PdReview saveReview = pdReviewRepository.save(pdReview);

                //review 이미지 저장
                PdReviewImg pdReviewImg = new PdReviewImg();
                pdReviewImg.setRevThumb("review"+sName);

                pdReviewImg.setRevNo(saveReview.getRevNo());

                pdReviewImgRepository.save(pdReviewImg);

                // 리뷰작성 포인트 지급
                // 0. orderDetail에서 포인트 얼마 지급할지 조회
                Optional<OrderDetail> optOrderDetail = orderdetailRepository.findById(Integer.parseInt(pdReviewDTO.getDetailNo()));
                //null 나오는걸 방지하기위해 Optional이라는 상자로 감쌈
                int detailPrice = 0;
                if (optOrderDetail.isPresent()) {
                    detailPrice = optOrderDetail.get().getDetailPrice();
                }
                int savePoint = (int) (Math.ceil(detailPrice * 0.01));

                // 1. UserPoint에 잔여포인트 합산
                String userId = pdReviewDTO.getUserId();
                UserPoint oldUserPoint = userPointRepository.findByUserId(userId);

                log.info("id로 조회한 oldUserPoint 엔티티 : " + oldUserPoint);
                log.info("원래 가지고 있던 포인트 : " + oldUserPoint.getPointBalance());
                log.info("적립할 포인트 : " + savePoint);

                oldUserPoint.setPointBalance(oldUserPoint.getPointBalance() + savePoint);
                log.info("변경된 포인트 : " + oldUserPoint.getPointBalance());

                userPointRepository.save(oldUserPoint);

                // 2. 포인트 히스토리 생성
                PointHistory pointHistory = new PointHistory();
                log.info("비어있는 pointHistory 엔티티 : " + pointHistory);

                pointHistory.setChangeCode("상품리뷰작성");
                pointHistory.setChangePoint(savePoint);
                pointHistory.setChangeType("적립");
                pointHistory.setPointNo(oldUserPoint.getPointNo());

                log.info("내용이 채워진 pointHistory 엔티티 : " + pointHistory);
                pointHistoryRepository.save(pointHistory);
                
                // 3. product 별점 업데이트
                Optional<Product> optProduct = productRepository.findById(pdReviewDTO.getProdNo());

                if(optProduct.isPresent()){

                    float oldScore = optProduct.get().getTReviewScore(); //현재 product에 리뷰점수
                    int starCount = optProduct.get().getTReviewCount(); //현재 product에 리뷰개수

                    log.info("현재 product에 리뷰점수 : " +oldScore);
                    log.info("현재 product에 리뷰개수 : " +starCount);

                    float totalScore = oldScore * starCount; //product 리뷰 총합점수

                    log.info("product 리뷰 총합점수 : " +totalScore);

                    float insertScore = (totalScore + pdReviewDTO.getRevScore()) / (starCount+1);

                    log.info("insertScore : " + insertScore);

                    optProduct.get().setTReviewCount(optProduct.get().getTReviewCount()+1);
                    optProduct.get().setTReviewScore(insertScore);
                    productRepository.save(optProduct.get());

                    log.info("optProduct : " +optProduct);
                }

            } catch (IOException e) {
                log.error(e.getMessage());
            }

        }else {
            return null;
        }

        return null;
    }

    //myReview 조회
    public PageResponseDTO selectReivews(String userId, PageRequestDTO pageRequestDTO){

        //페이징 처리
        Pageable pageable = pageRequestDTO.getPageable("no");

        return pdReviewRepository.selectReviews(userId, pageable, pageRequestDTO);
    }

    // my - wish 조회
    public PageResponseDTO selectUserWish(String userId, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> userWish = wishRepository.selectUserWish(userId, pageRequestDTO, pageable);

        List<WishDTO> wishDTOList = userWish.getContent().stream()
                .map(tuple -> {
                    Wish wish = tuple.get(0, Wish.class);
                    String prodName = tuple.get(1, String.class);
                    Integer prodPrice = tuple.get(2, Integer.class);
                    String thumb190 = tuple.get(3, String.class);
                    String cateCode = tuple.get(4, String.class);
                    WishDTO wishDTO = modelMapper.map(wish, WishDTO.class);
                    wishDTO.setProdName(prodName);
                    wishDTO.setProdPrice(prodPrice);
                    wishDTO.setThumb190(thumb190);
                    wishDTO.setCateCode(cateCode);
                    return wishDTO;
                        }
                    ).toList();

        log.info("wishDTOList : " + wishDTOList);

        List<WishDTO> resultWishDTO = new ArrayList<>();
        for (WishDTO eachWish : wishDTOList) {
            if (eachWish.getOptNo() != 0) {
                eachWish.setOptName(wishRepository.selectProdOption(eachWish.getOptNo()));
            }else {
                eachWish.setOptName("");
            }
            resultWishDTO.add(eachWish);
        }

        int total = (int) userWish.getTotalElements();
        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(resultWishDTO)
                .total(total)
                .build();
    }

    //myQna 조회
    public PageResponseDTO selectMyQna(String userId, PageRequestDTO pageRequestDTO){

        //고객문의 조회
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Qna> pageQna = qnaRepository.selectMyQna(userId, pageRequestDTO, pageable);

        List<QnaDTO> qnaDTOList = pageQna.getContent().stream()
                .map(qna -> {
                    QnaDTO qnaDTO = new QnaDTO();
                    qnaDTO.setQnaNo(qna.getQnaNo());
                    qnaDTO.setQnaTitle(qna.getQnaTitle());
                    qnaDTO.setQnaContent(qna.getQnaContent());
                    qnaDTO.setQnaDate(qna.getQnaDate());
                    qnaDTO.setQnaType(qna.getQnaType());
                    qnaDTO.setQnaCate(qna.getQnaCate());
                    qnaDTO.setQnaStatus(qna.getQnaStatus());
                    qnaDTO.setQnaReply(qna.getQnaReply());

                    return qnaDTO;
                })
                .toList();

        log.info("qnaDTOList : " +qnaDTOList);

        int total = (int) pageQna.getTotalElements();
        log.info("total : " +total);

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(qnaDTOList)
                .total(total)
                .build();

    }

    //myProdQna 조회
    public PageResponseDTO selectMyProdQna(String userId, PageRequestDTO pageRequestDTO){
        //상품문의 조회
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<ProdQna> pageProdQna = prodQnaRepository.selectMyProdQna(userId, pageRequestDTO, pageable);

        List<ProdQna> prodQnasList = pageProdQna.getContent();
        int total = (int) pageProdQna.getTotalElements();

        List<ProdQnaDTO> prodQnaDTOList = new ArrayList<>();
        for (ProdQna eachProdQna : prodQnasList) {
            prodQnaDTOList.add(modelMapper.map(eachProdQna, ProdQnaDTO.class));
        }

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(prodQnaDTOList)
                .total(total)
                .build();


    }

    //my정보 확인해주세요! 조회
    public UserDTO selectMyAdder(String userId){

        Optional<User> userList = userRepository.findById(userId);

        if (userList.isPresent()) {
            UserDTO userIdResult = modelMapper.map(userList.get(),UserDTO.class);
            return  userIdResult;
        }
        return null;
    }

    // 마이페이지 - 주문상세 모달 정보 조회
    public Map<String, List<?>> orderDetailCheck(int orderNo, OrderDetailDTO orderDetailDTO){

        log.info("orderDetailCheck service");

        return orderdetailRepository.selectDetailCheck(orderNo, orderDetailDTO);

    }

    //마이페이지 - 주문상태 업데이트
    public int updateOrderState(String prodNo, String detailNo){
        log.info("updateOrderState service");
        log.info("prodNo : "+prodNo);
        int orderDetailNo = Integer.parseInt(detailNo);

        Optional<OrderDetail> optOrderStatus = orderdetailRepository.findById(orderDetailNo);
        if(optOrderStatus.isPresent()){
            optOrderStatus.get().setDetailStatus("구매확정");
            OrderDetail result = orderdetailRepository.save(optOrderStatus.get());
            if(result.getDetailStatus().equals("구매확정")){
                return 1;
            }else {
                return 0;
            }
        }else {
            return 0;
        }
    }

    // 마이페이지 수취 확인
    public ResponseEntity<?> orderReceive(@PathVariable int detailNo) {
        Optional<OrderDetail> optOrderDetail = orderdetailRepository.findById(detailNo);
        Map<String, Integer> resultMap = new HashMap<>();
        if (optOrderDetail.isPresent()) {
            optOrderDetail.get().setDetailStatus("구매확정");
            OrderDetail saveOrderDetail = orderdetailRepository.save(optOrderDetail.get());
            if (saveOrderDetail.getDetailStatus().equals("구매확정")) {
                resultMap.put("result", 1);
                return ResponseEntity.status(HttpStatus.OK).body(resultMap);
            }else {
                resultMap.put("result", 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
            }
        }else {
            resultMap.put("result", 0);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        }
    }

    // 관심상품 삭제
    public void deleteWish(int[] wishNoArr) {
        for (int wishNo : wishNoArr) {
            wishRepository.deleteById(wishNo);
        }
    }

    // 관심상품 장바구니에 담기
    public ResponseEntity<?> wishProdToCart(Map<String, String> requestData) {
        String userId = requestData.get("userId");
        int prodNo = Integer.parseInt(requestData.get("prodNo"));
        int optNo = Integer.parseInt(requestData.get("optNo"));
        int count = Integer.parseInt(requestData.get("count"));
        Cart cart = cartRepository.findByUserId(userId);

        CartProduct cartProduct = new CartProduct();
        cartProduct.setCartNo(cart.getCartNo());
        cartProduct.setProdNo(prodNo);
        cartProduct.setCount(count);
        cartProduct.setOptNo(optNo);

        CartProduct saveCart = cartProductRepository.save(cartProduct);
        Map<String, Integer> resultMap = new HashMap<>();
        if (saveCart.getProdNo() == prodNo) {
            resultMap.put("result", 1);
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        }else {
            resultMap.put("result", 0);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        }
    }

    // 나의 설정 비밀번호 확인
    public ResponseEntity<?> checkUserPw(String userPw, String userId) {
        Optional<User> optUser = userRepository.findById(userId);
        Map<String, Integer> resultMap = new HashMap<>();
        if (optUser.isPresent()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean result = encoder.matches(userPw, optUser.get().getUserPw());
            if (result) {
                resultMap.put("result", 1);
                return ResponseEntity.status(HttpStatus.OK).body(resultMap);
            }else {
                resultMap.put("result", 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
            }
        }else {
            resultMap.put("result", 0);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        }
    }

    public ResponseEntity<?> selectMyProdQnaDetail(Map<String, Integer> requestBody){
        Integer prodQnaNo = requestBody.get("prodQnaNo");
        Integer prodNo = requestBody.get("prodNo");
        ProdQnaNote prodQnaNote = prodQnaNoteRepository.findByProdQnaNo(prodQnaNo);
        Tuple tuple = productRepository.selectProduct(prodNo);
        log.info(tuple.toString());
        Product product = tuple.get(0, Product.class);
        Productimg productimg = tuple.get(1, Productimg.class);
        if (product!=null){
            prodQnaNote.setProdNo(product.getProdNo());
            prodQnaNote.setProdName(product.getProdName());
            prodQnaNote.setThumb190(productimg.getThumb190());
        }

        if (prodQnaNote != null) {
            log.info("selectMyProdQnaDetail service:"+prodQnaNote);
            return ResponseEntity.status(HttpStatus.OK).body(prodQnaNote);
        }else {
            log.info("selectMyProdQnaDetail service :::: fail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // prodSeller 출력
    public ResponseEntity<?> selectProdSeller(String prodSeller){
        Optional<Seller> optSeller = sellerRepository.findById(prodSeller);
        Map<String, SellerDTO> resultMap = new HashMap<>();

        if(optSeller.isPresent()){
            SellerDTO sellerDTO = modelMapper.map(optSeller.get(), SellerDTO.class);
            log.info("sellerDTO : " + sellerDTO.toString());
            resultMap.put("result", sellerDTO);
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        }else {
            resultMap.put("result", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        }
    }

 }
