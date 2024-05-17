package kr.co.lotteon.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.FaqDTO;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.dto.QnaDTO;
import kr.co.lotteon.service.CsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CsController {

    private final CsService csService;


// cs-index //
    // cs 인덱스페이지 이동
    @GetMapping("/cs/index")
    public String csIndex(Model model){

        model.addAttribute("notice",csService.noticeList());
        model.addAttribute("qna",csService.qnaList());
        return "/cs/index";
    }

// cs-faq //
    // faq 글목록 이동
    @GetMapping("/cs/faq/list")
    public String faqList(Model model, String faqCate,String faqType){

        model.addAttribute("list",csService.faqList(faqCate));
        model.addAttribute("faqCate",faqCate);
        model.addAttribute("faqType",faqType);
        return "/cs/faq/list";
    }

    // faq 글보기
    @GetMapping("/cs/faq/view")
    public String faqView(Model model, int faqNo,String faqCate){
        FaqDTO faqDTO = csService.faqViewCate(faqCate);
        model.addAttribute("view",csService.faqView(faqNo));
        model.addAttribute("faqCate", faqCate);

        return "/cs/faq/view";
    }

    // cs-notice list //
    @GetMapping("/cs/notice/list")
    public String noticeList(Model model, PageRequestDTO pageRequestDTO, String noticeCate){

        log.info("noticeCate : " + noticeCate);


    PageResponseDTO pageResponseDTO = csService.selectNoticePages(pageRequestDTO, noticeCate);

    log.info("pageResponseDTO321321 : " + pageResponseDTO);
    model.addAttribute("pageResponseDTO", pageResponseDTO);
    model.addAttribute("noticeCate", noticeCate);

    return "/cs/notice/list";
}
    
    // notice 글보기
    @GetMapping("/cs/notice/view")
    public String noticeView(Model model, int noticeNo){
        model.addAttribute("view",csService.noticeView(noticeNo));
        return "/cs/notice/view";
    }

// cs-qna //
    // qna 글목록 이동
    @GetMapping("/cs/qna/list")
    public String qnaList(Model model, PageRequestDTO pageRequestDTO,String qnaCate){


        PageResponseDTO pageResponseDTO = csService.selectQnaPages(pageRequestDTO,qnaCate);
        log.info("qnaCate321 {}", qnaCate);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("qnaCate", qnaCate);

        log.info("pageResponseDTO321 {}", pageResponseDTO);
        return "/cs/qna/list";
    }

    // qna 글쓰기 이동
    @GetMapping("/cs/qna/write")
    public String qnaWriteForm(Model model,String qnaCate,String userId){

        log.info("QnaCate333 {}", qnaCate);
        model.addAttribute("qnaCate", qnaCate);
        model.addAttribute("userId", userId);
        return "/cs/qna/write";
    }

    // qna 글쓰기
    @PostMapping("/cs/qna/write")
    public String qnaWrite(QnaDTO qnaDTO){

        csService.insertQna(qnaDTO);
        log.info("QnaDTO {}", qnaDTO);

        return "redirect:/cs/qna/list?qnaCate=" + qnaDTO.getQnaCate();
    }
    // qna 글보기
    @GetMapping("/cs/qna/view")
    public String qnaView(Model model, int qnaNo){
        model.addAttribute("view",csService.qnaView(qnaNo));
        return "/cs/qna/view";
    }
}
