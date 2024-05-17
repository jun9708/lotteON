package kr.co.lotteon.controller.seller;

import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.dto.ProdQnaDTO;
import kr.co.lotteon.dto.ProdQnaNoteDTO;
import kr.co.lotteon.service.seller.SellerQnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SellerQnaController {

    private final SellerQnaService sellerQnaService;

////// 고객 센터 (seller/cs) //////
    // 판매자 관리페이지 - QNA List
    @GetMapping("/seller/cs/qna/list")
    public String sellerQnaList(String prodSeller, Model model, PageRequestDTO pageRequestDTO){

        PageResponseDTO prodQnaList = sellerQnaService.selectSellerQnaList(prodSeller, pageRequestDTO);

        model.addAttribute("prodQnaList", prodQnaList);
        log.info("prodQnaList : " + prodQnaList);

        return "/seller/cs/qna/list";
    }

    // 판매자 관리페이지 - QNA View
    @GetMapping("/seller/cs/qna/view")
    public String sellerQnaView(int qnaNo, Model model, PageRequestDTO pageRequestDTO){

        Map<String, Object> resultQna = sellerQnaService.selectSellerQnaView(qnaNo);

        ProdQnaDTO prodQnaDTO = (ProdQnaDTO) resultQna.get("prodQnaDTO");
        List<ProdQnaNoteDTO> prodQnaNoteList = (List<ProdQnaNoteDTO>) resultQna.get("prodQnaNoteList");

        model.addAttribute("prodQnaDTO",prodQnaDTO);
        model.addAttribute("prodQnaNoteList",prodQnaNoteList);

        return "/seller/cs/qna/view";
    }

    // 판매자 관리페이지 - QNA View - 상품 문의 답글 등록
    @PostMapping("/seller/cs/qna/insertQnaNote")
    public ResponseEntity<?> insertQnaNote(@RequestBody Map<String, String> requestData) {
        String content = requestData.get("qnaComment");
        String prodQnaNo = requestData.get("prodQnaNo");
        String sellerNo = requestData.get("sellerNo");
        ProdQnaNoteDTO prodQnaNoteDTO = new ProdQnaNoteDTO();
        prodQnaNoteDTO.setContent(content);
        prodQnaNoteDTO.setProdQnaNo(Integer.parseInt(prodQnaNo));
        prodQnaNoteDTO.setSellerNo(sellerNo);
        prodQnaNoteDTO.setCQnaDate(LocalDateTime.now());

        return sellerQnaService.insertQnaNote(prodQnaNoteDTO);
    }

    // 판매자 관리페이지 - QNA View - 상품 문의 답글 삭제
    @GetMapping("/seller/cs/qna/deleteQnaNote/{CQnaNo}")
    public ResponseEntity<?> deleteQnaNote(@PathVariable String CQnaNo){
        return sellerQnaService.deleteQnaNote(CQnaNo);
    }

    // 판매자 관리페이지 - QNA View - 상품 문의 답글 수정
    @PostMapping("/seller/cs/qna/modifyQnaNote")
    public ResponseEntity<?> modifyQnaNote(@RequestBody Map<String, String> requestData) {

        String content = requestData.get("qnaComment");
        String CQnaNo = requestData.get("CQnaNo");
        ProdQnaNoteDTO prodQnaNoteDTO = new ProdQnaNoteDTO();
        prodQnaNoteDTO.setContent(content);
        prodQnaNoteDTO.setCQnaNo(Integer.parseInt(CQnaNo));
        prodQnaNoteDTO.setCQnaDate(LocalDateTime.now());
        log.info("prodQnaNoteDTO : " + prodQnaNoteDTO);

        return sellerQnaService.modifyQnaNote(prodQnaNoteDTO);
    }

    // 판매자 관리페이지 - NOTICE List
    @GetMapping("/seller/cs/notice/list")
    public String sellerNoticeList(Model model, PageRequestDTO pageRequestDTO,
                                   @RequestParam(value = "noticeCate", required = false) String noticeCate,
                                   @RequestParam(value = "noticeType", required = false) String noticeType){
        PageResponseDTO noticeList = sellerQnaService.selectSellerNoticeList(pageRequestDTO, noticeCate,noticeType);

        model.addAttribute("noticeCate",noticeCate);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("noticeType", noticeType);
        return "/seller/cs/notice/list";
    }

    // 판매자 관리페이지 - NOTICE view
    @GetMapping("/seller/cs/notice/view")
    public String sellerNoticeView(Model model, int noticeNo){

        model.addAttribute("view",sellerQnaService.selectSellerNoticeView(noticeNo));
        return "/seller/cs/notice/view";
    }
}
