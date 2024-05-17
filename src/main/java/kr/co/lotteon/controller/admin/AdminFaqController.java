package kr.co.lotteon.controller.admin;

import kr.co.lotteon.dto.FaqDTO;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.service.admin.AdminFaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminFaqController {

    private final AdminFaqService adminFaqService;

    // 관리자 고객센터 자주묻는질문 리스트 출력
    @GetMapping("/admin/cs/faq/list")
    public String csFaqList(Model model, PageRequestDTO pageRequestDTO,
                            @RequestParam(value = "faqCate", required = false) String faqCate,
                            @RequestParam(value = "faqType", required = false) String faqType){

        PageResponseDTO pageResponseDTO = adminFaqService.FaqAdminSelect(pageRequestDTO,faqCate,faqType);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("faqCate",faqCate);
        model.addAttribute("faqType",faqType);

        return "/admin/cs/faq/list";
    }
    // 관리자 고객센터 자주묻는질문 등록폼
    @GetMapping("/admin/cs/faq/write")
    public String csFaqWriteForm(){
        return "/admin/cs/faq/write";
    }
    // 관리자 고객센터 자주묻는질문 등록
    @PostMapping("/admin/cs/faq/write")
    public String csFaqWrite(FaqDTO faqDTO){

        adminFaqService.FaqAdminInsert(faqDTO);
        return "redirect:/admin/cs/faq/list";
    }

    // 관리자 고객센터 자주묻는질문 수정폼 불러오기
    @GetMapping("/admin/cs/faq/modify")
    public String csFaqModifyForm(@RequestParam("faqNo") int faqNo, Model model){

        FaqDTO faqDTO = adminFaqService.FaqAdminView(faqNo);
        model.addAttribute("faq",faqDTO);
        return "/admin/cs/faq/modify";
    }
    // 관리자 고객센터 자주묻는질문 수정
    @PostMapping("/admin/cs/faq/modify")
    public String csFaqModify(@RequestParam int faqNo, FaqDTO faqDTO){

        log.info("수정 faqDTO {}", faqDTO);
        // 기존 글 조회
        FaqDTO faqViewDTO = adminFaqService.FaqAdminView(faqNo);

        // 수정된 내용 설정
        faqViewDTO.setFaqCate(faqDTO.getFaqCate());
        faqViewDTO.setFaqType(faqDTO.getFaqType());
        faqViewDTO.setFaqTitle(faqDTO.getFaqTitle());
        faqViewDTO.setFaqContent(faqDTO.getFaqContent());

        // 수정된 글 업데이트
        adminFaqService.faqAdminUpdate(faqViewDTO);

        return "redirect:/admin/cs/faq/list";
    }

    // 관리자 고객센터 자주묻는질문 글보기
    @GetMapping("/admin/cs/faq/view")
    public String csFaqView(Model model, int faqNo){

        model.addAttribute("view",adminFaqService.FaqAdminView(faqNo));
        return "/admin/cs/faq/view";
    }

    // 관리자 고객센터 자주묻는질문 글 삭제
    @GetMapping("/admin/cs/faq/delete")
    public String delete(int faqNo){
        adminFaqService.faqAdminDelete(faqNo);
        return "redirect:/admin/cs/faq/list";
    }

    // faq 게시글 선택 삭제
    @PostMapping("/admin/cs/faq/delete")
    public ResponseEntity<?> deleteFaq(@RequestBody Map<String, int[]> requestData){
        log.info("requestData : " + requestData);
        int[] faq_faqNos = requestData.get("faq_faqNo");
        log.info("22222");
        log.info("faq_faqNos = {}", Arrays.toString(faq_faqNos));
        return adminFaqService.deleteFaq(faq_faqNos);
    }
}
