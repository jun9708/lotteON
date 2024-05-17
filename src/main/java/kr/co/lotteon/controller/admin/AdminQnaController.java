package kr.co.lotteon.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.NoticeDTO;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.dto.QnaDTO;
import kr.co.lotteon.repository.admin.AdminQnaRepository;
import kr.co.lotteon.service.admin.AdminQnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminQnaController {

    private final AdminQnaService adminQnaService;

    // admin cs 문의글 목록
    @GetMapping("/admin/cs/qna/list")
    public String csQnaList(Model model, PageRequestDTO pageRequestDTO,
                            @RequestParam(value = "qnaCate", required = false) String qnaCate,
                            @RequestParam(value = "qnaType", required = false) String qnaType){

        PageResponseDTO pageResponseDTO = adminQnaService.qnaAdminSelect(pageRequestDTO,qnaCate,qnaType);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        model.addAttribute("qnaCate",qnaCate);
        model.addAttribute("qnaType",qnaType);

        return "/admin/cs/qna/list";
    }
    // admin cs 문의답변 폼
    @GetMapping("/admin/cs/qna/reply")
    public String csQnaReplyForm(@RequestParam("qnaNo") int qnaNo, Model model){

        QnaDTO qnaDTO = adminQnaService.QnaAdminView(qnaNo);
        model.addAttribute("qna",qnaDTO);

        return "/admin/cs/qna/reply";
    }
    // admin cs 문의답변
    @PostMapping("/admin/cs/qna/reply")
    public String csQnaReply(@RequestParam int qnaNo, QnaDTO qnaDTO){

        log.info("문의답변 qnaDTO {}", qnaDTO);
        // 기존 글 조회
        QnaDTO qnaViewDTO = adminQnaService.QnaAdminView(qnaNo);

        // 답변 내용 설정
        qnaViewDTO.setQnaReply(qnaDTO.getQnaReply());

        if(!qnaDTO.getQnaReply().isEmpty()){
            qnaViewDTO.setQnaStatus("답변완료");
        }

        // 답변 업데이트
        adminQnaService.qnaAdminReply(qnaViewDTO);

        return "redirect:/admin/cs/qna/list";
    }

    // admin cs 특정 문의글
    @GetMapping("/admin/cs/qna/view")
    public String csQnaView(Model model, int qnaNo){

        model.addAttribute("view",adminQnaService.QnaAdminView(qnaNo));
        return "/admin/cs/qna/view";
    }
    // admin cs 특정 문의글 삭제
    @GetMapping("/admin/cs/qna/delete")
    public String delete(int qnaNo){
        adminQnaService.qnaAdminDelete(qnaNo);
        return "redirect:/admin/cs/qna/list";
    }
    //게시물 선택 삭제
    @ResponseBody
    @PostMapping(value = "/admin/cs/qna/delete")
    public ResponseEntity<?> deleteChecked(@RequestBody Map<String, int[]> requestData){

        log.info("deleteChecked!");
        log.info("requestData : " + requestData);
        int[] ajaxMsg = requestData.get("qna_qnaNo");

        int size = ajaxMsg.length;
        log.info("size = {}", size);

        for(int i=0; i<size; i++){
            int intValue = ajaxMsg[i];
            log.info("invalues = {}", intValue);
            adminQnaService.qnaAdminDelete(intValue);
        }

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("data", "삭제 성공");

        return ResponseEntity.ok().body(resultMap);

    }
}
