package kr.co.lotteon.controller;

import groovy.util.logging.Slf4j;
import kr.co.lotteon.dto.TermsDTO;
import kr.co.lotteon.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PolicyController {

    private final TermsService termsService;

    // 구매회원 이용약관 이동
    @GetMapping("/policy/terms")
    public String policyTerms(String cate, Model model){
        List<TermsDTO> termsList = termsService.selectTermsForPolicy(cate);
        model.addAttribute("termsList", termsList);
        return "/policy/terms";
    }

    // 전자금융거래 이용약관 이동
    @GetMapping("/policy/finance")
    public String finance(){
        return "/policy/finance";
    }

    // 위치서비스 이용약관 이동
    @GetMapping("/policy/location")
    public String location(){
        return "/policy/location";
    }

    // 개인정보 이용약관 이동
    @GetMapping("/policy/privacy")
    public String privacy(){
        return "/policy/privacy";
    }

    // 판매회원 이용약관 이동
    @GetMapping("/policy/seller")
    public String seller(){
        return "/policy/seller";
    }

}
