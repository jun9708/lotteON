package kr.co.lotteon.controller.seller;

import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.*;
import kr.co.lotteon.service.admin.AdminProductService;
import kr.co.lotteon.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.AuthProvider;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SellerController {

    private final SellerService sellerService;
    private final AdminProductService adminproductService;


    /*
        판매자 관리페이지 - 홈 이동
        - orderDetail 테이블에서 prodSeller로 상품 검색
            - count(*) = 주문 건수
            - sum(detailPrice) = 총 주문 금액
            - detailStatus = [입금대기, 배송준비, 취소요청, 교환요청, 반품요청]
     */
    @GetMapping("/seller/index")
    public String sellerIndex(HttpSession session, Authentication authentication, Model model){
        String UserId = authentication.getName();
        // 기간별 주문 건수 & 주문 금액 & 배송 현황 집계 & prodSeller 세션 저장
        SellerInfoDTO sellerInfoDTO = sellerService.selectSellerInfo(session, UserId);

        String prodSeller = (String) session.getAttribute("prodSeller");
        // 최근 한달치 주문 건수
        LinkedHashMap<String, Integer> monthCount = sellerService.selectProdSalesCount(prodSeller);
        // 최근 한달 일자별 주문 금액 합산
        LinkedHashMap<String, Integer> monthPrice = sellerService.selectSalesForMonth(prodSeller);
        // 공지사항 최신순 5개 조회
        List<NoticeDTO> noticeDTO = sellerService.selectNoticeForIndex();
        // 고객문의 최신순 5개 조회
        List<ProdQnaDTO> prodQnaDTO = sellerService.selectProdQnaForIndex(prodSeller);

        model.addAttribute("sellerInfoDTO", sellerInfoDTO);
        model.addAttribute("monthCount", monthCount);
        model.addAttribute("monthPrice", monthPrice);
        model.addAttribute("noticeDTO", noticeDTO);
        model.addAttribute("prodQnaDTO", prodQnaDTO);

        return "/seller/index";
    }
////// 상품 관리 (seller/product) //////
    // 판매자 관리페이지 - 상품목록 - 상품관리
    @GetMapping("/seller/product/list")
    public String sellerProdList(String prodSeller, Model model, ProductPageRequestDTO productPageRequestDTO){

        ProductPageResponseDTO pageResponseDTO = null;
        if(productPageRequestDTO.getKeyword() == null) {
            // 판매자의 전체 상품 목록 조회
            pageResponseDTO = sellerService.selectProductForSeller(prodSeller, productPageRequestDTO);
        }else {
            // 판매자의 검색 상품 목록 조회
            pageResponseDTO = sellerService.searchProductForSeller(prodSeller, productPageRequestDTO);
        }
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        return "/seller/product/list";
    }

    // 판매자 상품 등록 이동
    @GetMapping("/seller/product/register")
    public String productRegister(){

        return "/seller/product/register";
    }

    // 판매자 상품 등록
    @PostMapping("/seller/product/register")
    public String registerProduct(ProductDTO productDTO, ProductimgDTO productimgDTO){
        log.info(productDTO.toString());
        log.info(productimgDTO.toString());
        adminproductService.registerProduct(productDTO, productimgDTO);

        return "redirect:/seller/product/register";
    }

    // 판매자 상품 옵션 등록 페이지 이동
    @GetMapping("/seller/product/option")
    public String registerProductOption(Model model, @RequestParam("prodNo") int prodNo){
        Map<String, Object> resultMap = adminproductService.selectProductOption(prodNo);
        ProductDTO productDTO = (ProductDTO) resultMap.get("productDTO");
        List<ProdOptionDTO> optionDTOList = (List<ProdOptionDTO>) resultMap.get("optionDTOList");
        List<prodOptDetailDTO> prodOptDetailDTOList = (List<prodOptDetailDTO>) resultMap.get("prodOptDetailDTOList");

        log.info("productDTO : " + productDTO);
        log.info("optionDTOList : " + optionDTOList);

        model.addAttribute("productDTO", productDTO);
        model.addAttribute("optionDTOList", optionDTOList);
        model.addAttribute("optionDetail", prodOptDetailDTOList);

        return "/seller/product/option";
    }
    
    // 판매자 상품 기본 정보 변경
    @PostMapping("/seller/product/modifyProdInfo")
    public String modifyProdInfo(ProductDTO productDTO, ProductimgDTO productimgDTO) {

        log.info("productDTO : " + productDTO);
        log.info("productimgDTO : " + productimgDTO);

        int result = adminproductService.modifyProdInfo(productDTO, productimgDTO);

        return "redirect:/seller/product/option?prodNo=" + productDTO.getProdNo();
    }

////// 주문 관리 (seller/order) //////
    // 판매자 주문 관리 //
    @GetMapping("/seller/order/orderList")
    public String SellerOrderList(String prodSeller, Model model, PageRequestDTO pageRequestDTO){

        // 최근 한달치 주문 건수
        LinkedHashMap<String, Integer> orderByDate = sellerService.selectProdSalesCount(prodSeller);

        // 판매자의 판매 목록 최신순
        PageResponseDTO selectOrder = sellerService.selectProdSalesInfo(prodSeller, pageRequestDTO);
        log.info("selectOrder : " + selectOrder);

        model.addAttribute("orderByDate", orderByDate);
        model.addAttribute("selectOrder", selectOrder);
        return "/seller/order/orderList";
    }

    // 판매자 매출 현황 //
    @GetMapping("/seller/order/sales")
    public String SellerSales(String prodSeller, Model model){

        // 최근 한달 일자별 주문 금액 합산
        LinkedHashMap<String, Integer> orderByDate = sellerService.selectSalesForMonth(prodSeller);
        // 판매자의 기간별 매출, 취소, 환불 금액 합산
        Map<String, Map<String, Integer>> resultMap = sellerService.selectSalesAverages(prodSeller);
        // 최근 일주일 일자별 주문 상세 (매출, 취소, 교환, 환불 금액 / 건수)
        LinkedHashMap<String, OrderPriceCountDTO> orderForWeek = sellerService.selectSalesForWeek(prodSeller);

        model.addAttribute("orderByDate", orderByDate);
        model.addAttribute("resultMap", resultMap);
        model.addAttribute("orderForWeek", orderForWeek);
        return "/seller/order/sales";
    }

    // 판매자 배송 관리 //
    @GetMapping("/seller/order/delivery")
    public String SellerDelivery(String prodSeller, Model model, PageRequestDTO pageRequestDTO){
        // 판매자의 판매 목록 최신순
        PageResponseDTO selectOrder = sellerService.selectProdSalesInfo(prodSeller, pageRequestDTO);
        log.info("selectOrder : " + selectOrder);

        SellerInfoDTO sellerInfoDTO = sellerService.selectAdminInfo();

        model.addAttribute("sellerInfoDTO", sellerInfoDTO);
        model.addAttribute("selectOrder", selectOrder);
        return "/seller/order/delivery";
    }

    // 판매 상품 주문 상태 변경 //
    @PostMapping("/seller/product/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestBody Map<String, String> requestData) {
        int detailNo = Integer.parseInt(requestData.get("detailNo"));
        String detailStatus = requestData.get("detailStatus");

        int result = sellerService.updateStatus(detailNo, detailStatus);
        if (result > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    // 판매 상품 판매 상태 변경 //
    @GetMapping("/seller/product/modifyProdStatus/{prodNo}/{status}")
    public ResponseEntity<?> modifyProdStatus(@PathVariable int prodNo, @PathVariable int status) {
        return sellerService.modifyProdStatus(prodNo, status);
    }

}
