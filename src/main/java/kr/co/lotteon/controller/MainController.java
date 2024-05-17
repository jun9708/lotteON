package kr.co.lotteon.controller;

import kr.co.lotteon.dto.*;
import kr.co.lotteon.service.ProdCateService;
import kr.co.lotteon.service.ProductService;
import kr.co.lotteon.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Console;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {

    private final ProdCateService prodCateService;
    private final ProductService productService;
    private final AdminService adminService;

    /* 
        메인페이지
         1. product 출력
         2. 
         3. 
     */
    @GetMapping("/")
    public String index(Model model){

        // index 페이지 product 출력
        String prodDiscount = "prodDiscount";
        String prodSold     = "prodSold";
        String prodRdate    = "prodRdate";
        String prodScore    = "prodScore";
        String prodHit      = "prodHit";
        String bannerMain2  = "main2";
        String bannerMain1  = "main1";
        log.info("bannerMain2222 : " + bannerMain2);
        log.info("bannerMain1111 : " + bannerMain1);
        List<ProductDTO> discountList = productService.selectIndexProducts(prodDiscount);
        List<ProductDTO> soldList = productService.selectIndexProducts(prodSold);
        List<ProductDTO> rDateList = productService.selectIndexProducts(prodRdate);
        List<ProductDTO> scoreList = productService.selectIndexProducts(prodScore);
        List<ProductDTO> hitList = productService.selectIndexProducts(prodHit);
        List<BannerDTO> bannerList2 = adminService.selectBanners(bannerMain2);
        List<BannerDTO> bannerList1 = adminService.selectBanners(bannerMain1);

        model.addAttribute("discount", discountList);
        model.addAttribute("sold", soldList);
        model.addAttribute("rDate", rDateList);
        model.addAttribute("score", scoreList);
        model.addAttribute("hit", hitList);
        model.addAttribute("bannerList2", bannerList2);
        model.addAttribute("bannerList1", bannerList1);
        return "/index";
    }
}