package kr.co.lotteon.controller;

import kr.co.lotteon.dto.CompanyStoryDTO;
import kr.co.lotteon.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/company/index")
    public String companyIndex(){

        return "/company/index";
    }

    @GetMapping("/company/story")
    public String companyStory(Model model, String storyCate){

        List<CompanyStoryDTO> companyStoryList = companyService.selectCompanyStory(storyCate);
        model.addAttribute("companyStoryList", companyStoryList);
        return "/company/story";
    }

    @GetMapping("/company/culture")
    public String companyCulture(){

        return "/company/culture";
    }

    @GetMapping("/company/recruit")
    public String companyRecruit(){

        return "/company/recruit";
    }

    @GetMapping("/company/media")
    public String companyMedia(){

        return "/company/media";
    }
}
