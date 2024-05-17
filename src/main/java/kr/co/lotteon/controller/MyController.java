package kr.co.lotteon.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.Coupons;
import kr.co.lotteon.entity.Orders;
import kr.co.lotteon.entity.PointHistory;
import kr.co.lotteon.entity.QOrderDetail;
import kr.co.lotteon.repository.SellerRepository;
import kr.co.lotteon.repository.UserPointRepository;
import kr.co.lotteon.repository.PointHistoryRepository;
import kr.co.lotteon.service.MyService;
import kr.co.lotteon.service.ProductService;
import kr.co.lotteon.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MyController {

    private final MyService myService;
    private final UserPointRepository userPointRepository;
    private final ProductService productService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final SellerRepository sellerRepository;

    //마이페이지-홈 이동
    @GetMapping("/my/home")
    public String myHome(HttpSession session, Model model, String userId){

        // 회원 정보
        myService.selectMyInfo(session, userId);


        // 최근주문내역
        LinkedHashMap<Integer, List<OrderDetailDTO>> myOrder = myService.myHomeSelectOrder(userId);
        model.addAttribute("myOrder", myOrder);
        log.info("myOrder" +myOrder);


        // 포인트적립내역
        List<PointHistoryDTO> myPoint = myService.myHomeselectPoints(userId);
        model.addAttribute("myPoint", myPoint);

        // 상품평
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setSize(5);
        PageResponseDTO myReviewPage = myService.selectReivews(userId, pageRequestDTO);

        model.addAttribute("myReviewPage", myReviewPage);

        // 문의내역
        pageRequestDTO.setCate("qna");
        pageRequestDTO.setSize(5);
        PageResponseDTO selectMyQna = myService.selectMyQna(userId, pageRequestDTO);
        model.addAttribute("selectMyQna", selectMyQna);

        //확인해주세요! 기능
        UserDTO resultUserId = myService.selectMyAdder(userId);
        log.info("resultUserId" +resultUserId);
        model.addAttribute("resultUserId", resultUserId);

        String banImgCate = "my1";
        List<BannerDTO> banMyOrderList = adminService.selectBanners(banImgCate);
        model.addAttribute("banMyOrderList", banMyOrderList);

        return "/my/home";

    }

    //마이페이지-쿠폰 이동
    @GetMapping("/my/coupon")
    public String myCoupon(PageRequestDTO pageRequestDTO, String userId, Model model){

        PageResponseDTO haveCoupons = myService.selectCoupons(pageRequestDTO, userId);
        model.addAttribute("haveCoupons", haveCoupons); // Map<String ,List<Coupons>>  / Map<String, int>

        String banImgCate = "my1";
        List<BannerDTO> banMyOrderList = adminService.selectBanners(banImgCate);
        model.addAttribute("banMyOrderList", banMyOrderList);
        return "/my/coupon";
    }

    //마이페이지-정보 이동
    @GetMapping("/my/info")
    public String myInfo(Model model, String userId){
        Map<String, Object> result = myService.selectUserInfo(userId);
        UserDTO userDTO = (UserDTO) result.get("user");
        SellerDTO sellerDTO = (SellerDTO) result.get("seller");

        String banImgCate = "my1";
        List<BannerDTO> banMyOrderList = adminService.selectBanners(banImgCate);
        model.addAttribute("banMyOrderList", banMyOrderList);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("sellerDTO", sellerDTO);
        return "/my/info";
    }

    // 마이페이지 - 판매자 이름 수정
    @PostMapping("/my/updateSellerName")
    public ResponseEntity<?> myInfoUpdateSellerName(@RequestBody Map<String, String> requestData){
        String userId = requestData.get("userId");
        String sellerName = requestData.get("sellerName");
        log.info("userId : " + userId);
        log.info("sellerName : " + sellerName);
        return myService.myInfoUpdateSellerName(userId, sellerName);
    }
    // 마이페이지 - 판매자 연락처 수정
    @PostMapping("/my/updateSellerHp")
    public ResponseEntity<?> myInfoUpdateSellerHp(@RequestBody Map<String, String> requestData){
        String userId = requestData.get("userId");
        String sellerHp = requestData.get("sellerHp");
        log.info("userId : " + userId);
        log.info("sellerHp : " + sellerHp);
        return myService.myInfoUpdateSellerHp(userId, sellerHp);
    }
    // 마이페이지 - 판매자 팩스번호 수정
    @PostMapping("/my/updateFax")
    public ResponseEntity<?> myInfoUpdateFax(@RequestBody Map<String, String> requestData){
        String userId = requestData.get("userId");
        String fax = requestData.get("fax");
        log.info("userId : " + userId);
        log.info("fax : " + fax);
        return myService.myInfoUpdateFax(userId,fax);
    }

    // 마이페이지 - 연락처 수정
    @PostMapping("/my/updateHp")
    public ResponseEntity<?> myInfoUpdateHp(@RequestBody Map<String, String> requestData){

        String userId = requestData.get("userId");
        String userHp = requestData.get("userHp");
        log.info("userId : " + userId);
        log.info("userHp : " + userHp);
        return myService.myInfoUpdateHp(userId, userHp);
    }

    // 마이페이지 - 이메일 수정
    @PostMapping("/my/updateEmail")
    public ResponseEntity<?> myInfoUpdateEmail(@RequestBody Map<String, String> requestData){

        String userId = requestData.get("userId");
        String userEmail = requestData.get("userEmail");
        log.info("userId : " + userId);
        log.info("userEmail : " + userEmail);
        return myService.myInfoUpdateEmail(userId, userEmail);
    }

    // 마이페이지 - 비밀번호 수정
    @PostMapping("/my/updatePw")
    public ResponseEntity<?> myInfoUpdatePw(@RequestBody Map<String, String> requestData){

        String userId = requestData.get("userId");
        String userPw = requestData.get("userPw");
        log.info("userId : " + userId);
        log.info("userPw : " + userPw);
        String encodedPassword = passwordEncoder.encode(userPw);
        return myService.myInfoUpdatePw(userId, encodedPassword);
    }
    // 마이페이지 - 주소 수정
    @PostMapping("/my/updateAddr")
    public ResponseEntity<?> myInfoUpdateAddr(@RequestBody UserDTO userDTO){
        log.info("here...! :" + userDTO);
        return myService.myInfoUpdateAddr(userDTO);
    }
    // 마이페이지 - 회원 탈퇴
    @PostMapping("/my/updateRole")
    public ResponseEntity<?> myInfoUpdateRole(@RequestBody Map<String, String> requestData){

        String userId = requestData.get("userId");
        String userRole = requestData.get("userRole");
        return myService.myInfoUpdateRole(userId,userRole);
    }

    //마이페이지 - 주문내역 이동
    @GetMapping("/my/order")
    public String myOrder(String userId, Model model, MyOrderPageRequestDTO myOrderPageRequestDTO){
        log.info(userId);
        log.info(myOrderPageRequestDTO.toString());

        MyOrderPageResponseDTO MyOrderDTOList = myService.selectOrders(userId, myOrderPageRequestDTO);
        model.addAttribute("MyOrderDTOList", MyOrderDTOList);

        String banImgCate = "my1";
        List<BannerDTO> banMyOrderList = adminService.selectBanners(banImgCate);
        model.addAttribute("banMyOrderList", banMyOrderList);

        return "/my/order";
    }

    //마이페이지 - 포인트 이동
    @GetMapping("/my/point")
    public String myPoint(String userId,
                          Model model,
                          PageRequestDTO pageRequestDTO){

        PageResponseDTO pageResponseDTO = myService.selectPoints(userId, pageRequestDTO);
        //userPointRepository.selectPoints(userId, pageRequestDTO);

        String banImgCate = "my1";
        List<BannerDTO> banMyOrderList = adminService.selectBanners(banImgCate);
        model.addAttribute("banMyOrderList", banMyOrderList);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

        return "/my/point";
    }

    //마이페이지-QnA 이동
    @GetMapping("/my/qna")
    public String myQna(String userId, PageRequestDTO pageRequestDTO, Model model){

        String banImgCate = "my1";
        List<BannerDTO> banMyOrderList = adminService.selectBanners(banImgCate);
        model.addAttribute("banMyOrderList", banMyOrderList);

        if (pageRequestDTO.getCate().equals("qna")) {

            PageResponseDTO selectMyQna = myService.selectMyQna(userId, pageRequestDTO);
            model.addAttribute("selectMyQna", selectMyQna);

            log.info("selectMyQna : " +selectMyQna);

            return "/my/qna";

        } else if(pageRequestDTO.getCate().equals("prodqna")) {

            PageResponseDTO selectMyProdQna = myService.selectMyProdQna(userId, pageRequestDTO);
            model.addAttribute("selectMyProdQna", selectMyProdQna);
            log.info("selectMyProdQna : " +selectMyProdQna);

            return "/my/qna";
        }
        return "/my/qna";
    }

    //마이페이지-리뷰 작성
    @PostMapping("/my/writeReview")
    public String writeReview(Authentication authentication, PdReviewDTO pdReviewDTO, MultipartFile revImage){
        pdReviewDTO.setUserId(authentication.getName());

        log.info("pdReviewDTO : " + pdReviewDTO);
        log.info("revImage : " + revImage);

        myService.writeReview(pdReviewDTO, revImage);

        return "redirect:/my/review?userId=" + pdReviewDTO.getUserId();
    }

    //마이페이지-리뷰 이동
    @GetMapping("/my/review")
    public String myReview(String userId, Model model, PageRequestDTO pageRequestDTO){
        PageResponseDTO myReviewPage = myService.selectReivews(userId, pageRequestDTO);

        model.addAttribute("myReviewPage", myReviewPage);

        String banImgCate = "my1";
        List<BannerDTO> banMyOrderList = adminService.selectBanners(banImgCate);
        model.addAttribute("banMyOrderList", banMyOrderList);

        return "/my/review";
    }

    // 마이페이지 - 관심상품
    @GetMapping("/my/wish")
    public String myWish(String userId, Model model, PageRequestDTO pageRequestDTO){
        PageResponseDTO wishList = myService.selectUserWish(userId, pageRequestDTO);
        model.addAttribute("wishList", wishList);

        String banImgCate = "my1";
        List<BannerDTO> banMyOrderList = adminService.selectBanners(banImgCate);
        model.addAttribute("banMyOrderList", banMyOrderList);

        return "/my/wish";
    }

    // 마이페이지 - 주문상세 모달 정보 조회
    @GetMapping("/my/home/orderDetailCheck/{orderNo}")
    public ResponseEntity<?> orderDetailCheck(@PathVariable int orderNo, OrderDetailDTO orderDetailDTO) {

        log.info("orderNo : " + orderNo);
        Map<String, List<?>> resultMap = myService.orderDetailCheck(orderNo, orderDetailDTO);

        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        //List<Orders> orderNoInfo = (List<Orders>) resultMap.get("orderNoInfo");
        //List<OrderDetailDTO> orderDetailDTOList = (List<OrderDetailDTO>) resultMap.get("orderDetailDTOList");
    }
    
    //마이페이지 - 주문상태 업데이트
    @PostMapping("/my/updateOrder/")
    public ResponseEntity<?> orderStatusUpdate(@RequestBody Map<String, String> requestData){

        String prodNo = requestData.get("prodNo");
        String detailNo = requestData.get("detailNo");

        log.info("4894prodNo : " + prodNo);
        log.info("prodNo" +prodNo);
        int result = myService.updateOrderState(prodNo, detailNo);


        if(result > 0){
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    // 마이페이지 수취 확인
    @GetMapping("/my/orderReceive/{detailNo}")
    public ResponseEntity<?> orderReceive(@PathVariable int detailNo) {
        return myService.orderReceive(detailNo);
    }

    // 마이페이지 수취 확인
    @PostMapping("/my/writeProdQna")
    public String writeProdQna(ProdQnaDTO prodQnaDTO) {
        log.info("prodQnaDTO : " + prodQnaDTO);
        prodQnaDTO.setProdQnaStatus("답변 대기중");
        prodQnaDTO.setProdQnaSecret("N");
        int result = productService.writeProdQna(prodQnaDTO);
        if (result > 0) {
            return "redirect:/my/home?userId=" + prodQnaDTO.getUserId();
        }else {
            return "redirect:/my/home?userId=" + prodQnaDTO.getUserId() + "&err=100";
        }
    }

    // 관심상품 삭제
    @PostMapping("/my/wish/deleteWish")
    public ResponseEntity<?> deleteWish(@RequestBody Map<String, int[]> requestData) {
        int[] wishNoArr = requestData.get("wishNoArr");
        myService.deleteWish(wishNoArr);
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", 1);
        return ResponseEntity.ok().body(resultMap);
    }

    // 관심상품 장바구니에 담기
    @PostMapping("/my/wish/wishProdToCart")
    public ResponseEntity<?> wishProdToCart(@RequestBody Map<String, String> requestData) {
        log.info("requestData : " + requestData);
        return myService.wishProdToCart(requestData);
    }

    // 나의 설정 비밀번호 확인
    @GetMapping("/my/info/checkUserPw/{userPw}/{userId}")
    public ResponseEntity<?> checkUserPw(@PathVariable String userPw, @PathVariable String userId) {
        return myService.checkUserPw(userPw, userId);
    }

    // 마이 페이지 제품문의 보기
    @PostMapping("/my/selectProdQna")
    public ResponseEntity<?> selectProdQna(@RequestBody Map<String, Integer> requestBody) {

        return myService.selectMyProdQnaDetail(requestBody);
    }

    //Seller 정보 확인
    @GetMapping("/my/sellerCheck/{prodSeller}")
    public ResponseEntity<?> selectMyHomeSeller(@PathVariable String prodSeller){
        return myService.selectProdSeller(prodSeller);
    }

}
