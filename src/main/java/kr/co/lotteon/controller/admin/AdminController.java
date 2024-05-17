package kr.co.lotteon.controller.admin;

import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.Banner;
import kr.co.lotteon.entity.ProdOptDetail;
import kr.co.lotteon.repository.ProductRepository;
import kr.co.lotteon.service.MemberService;
import kr.co.lotteon.service.TermsService;
import kr.co.lotteon.service.admin.AdminProductService;
import kr.co.lotteon.service.admin.AdminService;
import kr.co.lotteon.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;
    private final SellerService sellerService;
    private final MemberService memberService;
    private final TermsService termsService;
    private final AdminProductService adminproductService;

    // 관리자 인덱스페이지 이동
    @GetMapping("/admin/index")
    public String adminIndex(Model model) {

        String prodSeller = "ADMIN";
        // 기간별 주문 건수 & 주문 금액 & 배송 현황 집계
        SellerInfoDTO sellerInfoDTO = sellerService.selectAdminInfo();
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

        return "/admin/index";
    }

    // 관리자 설정 정보 이동
    @GetMapping("/admin/config/info")
    public String configInfo(Model model) {
        TermsDTO termsDTO = termsService.selectTerms();
        model.addAttribute("termsDTO", termsDTO);
        return "/admin/config/info";
    }

    // admin - product //
    // 관리자 상품 목록 이동
    @GetMapping("/admin/product/list")
    public String productList(Model model, ProductPageRequestDTO productPageRequestDTO) {

        String prodSeller = "ADMIN";
        ProductPageResponseDTO pageResponseDTO = null;
        if(productPageRequestDTO.getKeyword() == null) {
            // 판매자의 전체 상품 목록 조회
            pageResponseDTO = sellerService.selectProductForSeller(prodSeller, productPageRequestDTO);
        }else {
            // 판매자의 검색 상품 목록 조회
            pageResponseDTO = sellerService.searchProductForSeller(prodSeller, productPageRequestDTO);
        }

        model.addAttribute("pageResponseDTO", pageResponseDTO);
        log.info("here....!!!" + pageResponseDTO);

        return "/admin/product/list";
    }

    // 관리자 상품 등록 이동
    @GetMapping("/admin/product/register")
    public String productRegister() {

        return "/admin/product/register";
    }

    // 관리자 상품등록시 대분류에 따른 중분류 자동출력
    @PutMapping("/admin/product/cate")
    public ResponseEntity<?> changeCate(@RequestBody Cate02DTO cate02DTO) {
        String cate01No = cate02DTO.getCate01No();
        try {
            ResponseEntity<List<Cate02DTO>> selectedCate02 = adminService.selectCate02(cate01No);
            List<Cate02DTO> cate02DTOList = selectedCate02.getBody();

            return ResponseEntity.ok().body(cate02DTOList);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }
    }

    // 관리자 상품 등록
    @PostMapping("/admin/product/register")
    public String registerProduct(ProductDTO productDTO, ProductimgDTO productimgDTO) {
        log.info(productDTO.toString());
        log.info(productimgDTO.toString());
        adminproductService.registerProduct(productDTO, productimgDTO);

        return "redirect:/admin/product/register";
    }

    // 관리자 상품 옵션 등록 페이지 이동
    @GetMapping("/admin/product/option")
    public String registerProductOption(Model model, @RequestParam("prodNo") int prodNo) {
        Map<String, Object> resultMap = adminproductService.selectProductOption(prodNo);
        ProductDTO productDTO = (ProductDTO) resultMap.get("productDTO");
        List<ProdOptionDTO> optionDTOList = (List<ProdOptionDTO>) resultMap.get("optionDTOList");

        log.info("productDTO : " + productDTO);
        log.info("optionDTOList : " + optionDTOList);

        model.addAttribute("productDTO", productDTO);
        model.addAttribute("optionDTOList", optionDTOList);

        return "/admin/product/option";
    }

    /*
        상품 옵션 등록
        - 사용자가 입력한 상품 옵션 (최대 3개까지) / prodOption1,2,3
            - [상품 번호, 옵션 이름, 옵션 값]
            ex) [ [22, 사이즈, small] / [22, 사이즈, large] / [22, 색상, black] / [22, 색상, blue] ... ]

        - 사용자가 입력한 상품 옵션의 연관관계(prodOptDetail)
            - [번호, 옵션1 값, 옵션2 값, 옵션3 값, 가격, 재고]
            ex) [ [1, small, black, 남성, 2000원, 100개] / [1, small, black, 여성, 3000원, 100개] ... ]

        - body에서 json데이터 꺼낸 후 각각 이중 List로 변환
            - 자바스크립트에서 배열로 데이터를 전송했기 떄문에 이중 List로 변환

        - 각각의  이중 List들을 List<DTO>로 변환 후 service로 전달
     */
    @PostMapping("/admin/product/regOption")
    public ResponseEntity<?> registerOption(@RequestBody Map<String, Object> requestData) {
        ArrayList<ArrayList<String>> prodOption1List = (ArrayList<ArrayList<String>>) requestData.get("prodOption1");
        ArrayList<ArrayList<String>> prodOption2List = (ArrayList<ArrayList<String>>) requestData.get("prodOption2");
        ArrayList<ArrayList<String>> prodOption3List = (ArrayList<ArrayList<String>>) requestData.get("prodOption3");
        ArrayList<ArrayList<String>> prodOptionDTOList = (ArrayList<ArrayList<String>>) requestData.get("prodOptionDTO");
        int prodNo = Integer.parseInt(prodOption1List.get(0).get(0));

        // option1 List<ProdOptionDTO>로 변환
        List<ProdOptionDTO> optionDTO1 = new ArrayList<>();
        for (ArrayList<String> eachOption : prodOption1List) {
            ProdOptionDTO optionDTO = new ProdOptionDTO();
            optionDTO.setProdNo(Integer.parseInt(eachOption.get(0)));
            optionDTO.setOptName(eachOption.get(1));
            optionDTO.setOptValue(eachOption.get(2));
            optionDTO1.add(optionDTO);
        }
        log.info("optionDTO1 : " + optionDTO1);

        // option2 List<ProdOptionDTO>로 변환
        List<ProdOptionDTO> optionDTO2 = new ArrayList<>();
        for (ArrayList<String> eachOption : prodOption2List) {
            ProdOptionDTO optionDTO = new ProdOptionDTO();
            optionDTO.setProdNo(Integer.parseInt(eachOption.get(0)));
            optionDTO.setOptName(eachOption.get(1));
            optionDTO.setOptValue(eachOption.get(2));
            optionDTO2.add(optionDTO);
        }
        log.info("optionDTO2 : " + optionDTO2);

        // option3 List<ProdOptionDTO>로 변환
        List<ProdOptionDTO> optionDTO3 = new ArrayList<>();
        for (ArrayList<String> eachOption : prodOption3List) {
            ProdOptionDTO optionDTO = new ProdOptionDTO();
            optionDTO.setProdNo(Integer.parseInt(eachOption.get(0)));
            optionDTO.setOptName(eachOption.get(1));
            optionDTO.setOptValue(eachOption.get(2));
            optionDTO3.add(optionDTO);
        }
        log.info("optionDTO3 : " + optionDTO3);

        // prodOptDetail List<prodOptDetailDTO>로 변환
        List<prodOptDetailDTO> optDetailDTOS = new ArrayList<>();
        for (ArrayList<String> eachDetail : prodOptionDTOList) {
            prodOptDetailDTO OptDetailDTO = new prodOptDetailDTO();
            OptDetailDTO.setProdNo(prodNo);
            OptDetailDTO.setOptDetailNo(Integer.parseInt(eachDetail.get(0)));
            OptDetailDTO.setOptPrice(Integer.parseInt(eachDetail.get(4)));
            OptDetailDTO.setOptStock(Integer.parseInt(eachDetail.get(5)));
            optDetailDTOS.add(OptDetailDTO);
        }
        log.info("optDetailDTOS : " + optDetailDTOS);

        return adminproductService.registerProdOption(optionDTO1, optionDTO2, optionDTO3, optDetailDTOS);
    }

    // 상품 카테고리 수정
    @PostMapping("/admin/product/modifyCate")
    public ResponseEntity<?> modifyCate(@RequestBody Map<String, Object> requestData) {
        return adminproductService.modifyCate(requestData);
    }

    // admin - config //
    // 관리자 설정 배너 이동
    @GetMapping("/admin/config/banner")
    public String configBanner(Model model) {

        // 카테고리별로 model참조 5번
        List<BannerDTO> main1 = adminService.bannerList("main1");
        List<BannerDTO> main2 = adminService.bannerList("main2");
        List<BannerDTO> product1 = adminService.bannerList("product1");
        List<BannerDTO> member1 = adminService.bannerList("member1");
        List<BannerDTO> my1 = adminService.bannerList("my1");

        model.addAttribute("main1", main1);
        model.addAttribute("main2", main2);
        model.addAttribute("product1", product1);
        model.addAttribute("member1", member1);
        model.addAttribute("my1", my1);

        return "/admin/config/banner";
    }

    // 배너 등록
    @PostMapping("/admin/banner/register")
    public String bannerRegister(BannerDTO bannerDTO, MultipartFile file) {

        log.info("bannerDTO1 : " + bannerDTO);
        log.info("bannerImg : " + file);

        // 이미지 파일
        String bannerPath = adminService.bannerUpload(bannerDTO.getBanImgCate(), file);
        bannerDTO.setBanImgName(bannerPath);
        bannerDTO.setBanUsable("yes".toUpperCase());
        // 나머지 글자 정보들
        log.info("bannerDTO2 : " + bannerDTO);

        if (bannerPath != null) {
            BannerDTO saveBanner = adminService.bannerInsert(bannerDTO);
            if (saveBanner.getBanNo() > 0) {
                //저장 성공
                return "redirect:/admin/config/banner";
            } else {
                // 저장실패
                return "redirect:/admin/config/banner?fail=100";
            }
        } else {
            // 저장실패
            return "redirect:/admin/config/banner?fail=100";
        }
    }

    // 배너 삭제
    @PostMapping("/admin/banner/delete")
    public ResponseEntity<?> deleteBanner(@RequestBody Map<String, int[]> requestData) {
        log.info("requestData :" + requestData);
        int[] banNos = requestData.get("banNos");
        log.info("banNos {}", banNos);
        return adminService.deleteBanner(banNos);
    }

    // 배너 활성화 버튼
    @PostMapping("/admin/banner/banUsable")
    public ResponseEntity<?> bannerUsableUpdate(@RequestBody Map<String, String> requestData){

        int banNo = Integer.parseInt( requestData.get("banNo"));
        String banImgCate = requestData.get("banImgCate");

        log.info("banNo :" + banNo);
        log.info("banImgCate :" + banImgCate);

        return adminService.updateBanners(banImgCate,banNo);
    }

    //메인 배너 상태 업데이트
    @PostMapping("/admin/banner/updateBanUsable")
    public ResponseEntity<?> updateMainBanUsable(@RequestBody Map<String, Object> requestData){
        String banNoStr = requestData.get("banNo").toString(); // banNo를 문자열로 가져옴
        int banNo = Integer.parseInt(banNoStr); // 문자열을 정수로 변환
        String banUsable = requestData.get("banUsable").toString(); // banUsable을 문자열로 가져옴

        log.info("banNo: " + banNo);
        log.info("banUsable: " + banUsable);

        return adminService.updateMainBanners(banNo, banUsable);

    }


//// admin - 주문관리 ////
    // 관리자 주문 관리
    @GetMapping("/admin/order/orderList")
    public String SellerOrderList(Model model, PageRequestDTO pageRequestDTO){
        String prodSeller = "ADMIN";
        // 최근 한달치 주문 건수
        LinkedHashMap<String, Integer> orderByDate = sellerService.selectProdSalesCount(prodSeller);

        // 판매자의 판매 목록 최신순
        PageResponseDTO selectOrder = sellerService.selectProdSalesInfo(prodSeller, pageRequestDTO);
        log.info("selectOrder : " + selectOrder);

        model.addAttribute("orderByDate", orderByDate);
        model.addAttribute("selectOrder", selectOrder);
        return "/admin/order/orderList";
    }

    // 관리자 매출 현황 //
    @GetMapping("/admin/order/sales")
    public String SellerSales(Model model){
        String prodSeller = "ADMIN";
        // 최근 한달 일자별 주문 금액 합산
        LinkedHashMap<String, Integer> orderByDate = sellerService.selectSalesForMonth(prodSeller);
        // 판매자의 기간별 매출, 취소, 환불 금액 합산
        Map<String, Map<String, Integer>> resultMap = sellerService.selectSalesAverages(prodSeller);
        // 최근 일주일 일자별 주문 상세 (매출, 취소, 교환, 환불 금액 / 건수)
        LinkedHashMap<String, OrderPriceCountDTO> orderForWeek = sellerService.selectSalesForWeek(prodSeller);

        model.addAttribute("orderByDate", orderByDate);
        model.addAttribute("resultMap", resultMap);
        model.addAttribute("orderForWeek", orderForWeek);
        return "/admin/order/sales";
    }

    // 판매자 배송 관리 //
    @GetMapping("/admin/order/delivery")
    public String SellerDelivery(Model model, PageRequestDTO pageRequestDTO){
        String prodSeller = "ADMIN";
        // 판매자의 판매 목록 최신순
        PageResponseDTO selectOrder = sellerService.selectProdSalesInfo(prodSeller, pageRequestDTO);
        log.info("selectOrder : " + selectOrder);

        SellerInfoDTO sellerInfoDTO = sellerService.selectAdminInfo();

        model.addAttribute("selectOrder", selectOrder);
        model.addAttribute("sellerInfoDTO", sellerInfoDTO);
        return "/admin/order/delivery";
    }

//// admin - 상점관리 ////
    // 관리자 - 상점관리 - 판매자현황 //
    @GetMapping("/admin/store/list")
    public String storeList(Model model, PageRequestDTO pageRequestDTO){

        PageResponseDTO sellerDTO = sellerService.selectSellerList(pageRequestDTO);
        model.addAttribute("sellerDTO", sellerDTO);
        return "/admin/store/list";
    }


//// admin - 회원관리 ////
    // 관리자 - 회원관리 - 회원현황 //
    @GetMapping("/admin/user/list")
    public String userList(Model model, PageRequestDTO pageRequestDTO){

        PageResponseDTO userDTO = memberService.selectMemberList(pageRequestDTO);
        model.addAttribute("userDTO", userDTO);
        return "/admin/user/list";
    }

    // 관리자 - 회원관리 - 회원 정보 변경 //
    @PostMapping("/admin/user/changeInfo")
    public ResponseEntity<?> changeInfo(@RequestBody Map<String, String> requestData){
        String userId = requestData.get("userId");
        String changeType = requestData.get("changeType");
        String changeValue = requestData.get("changeValue");

        return memberService.changeUserInfo(userId, changeType, changeValue);
    }

    // 관리자 - 회원관리 - 포인트관리 //
    @GetMapping("/admin/user/point")
    public String userPointControl(Model model, PageRequestDTO pageRequestDTO) {

        PageResponseDTO ResponseDTO = memberService.userPointControl(pageRequestDTO);
        model.addAttribute("ResponseDTO", ResponseDTO);
        return "/admin/user/point";
    }

    // 관리자 - 회원관리 - 포인트관리 - 포인트 지급 & 회수 //
    @PostMapping("/admin/user/point/control")
    public ResponseEntity<?> pointControl(@RequestBody Map<String, String> requestData) {
        String type = requestData.get("type");
        String userId = requestData.get("userId");
        String changePoint = requestData.get("changePoint");
        String changeCode = requestData.get("changeCode");

        return memberService.pointControl(type, userId, changePoint, changeCode);
    }

    // 관리자 - 회원관리 - 접속자 집계 //
    @GetMapping("/admin/user/counting")
    public String UserCounting(){

        return "/admin/user/counting";
    }
}