package kr.co.lotteon.service.admin;

import kr.co.lotteon.dto.FaqDTO;
import kr.co.lotteon.dto.NoticeDTO;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.entity.Faq;
import kr.co.lotteon.entity.Notice;
import kr.co.lotteon.repository.admin.AdminFaqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminFaqService {

    private final AdminFaqRepository adminFaqRepository;
    private final ModelMapper modelMapper;

    // 관리자페이지 고객센터 메뉴 자주묻는질문 글등록
    public void FaqAdminInsert(FaqDTO faqDTO) {

        // noticeDTO를 Notice엔티티로 변환
        Faq faq = modelMapper.map(faqDTO, Faq.class);
        log.info("faq ={}", faq.toString());
        // 매핑된 Notice 엔티티를 DB에 저장
        Faq savedFaq = adminFaqRepository.save(faq);
        log.info("savedFaq ={}", savedFaq.toString());
    }

    // 관리자페이지 고객센터 메뉴 자주묻는질문 특정 글 불러오기
    @Transactional
    public FaqDTO FaqAdminView(int faqNo) {

        // .orElse(null); optional 객체가 비어있을경우 대비
        Faq faq = adminFaqRepository.findById(faqNo).orElse(null);
        log.info("특정 글 불러오기 modify {}", faq);
        if (faq != null) {
            // DTO로 변환후에 반환
            adminFaqRepository.incrementHitByFaqNo(faqNo);
            return modelMapper.map(faq, FaqDTO.class);
        }
        return null; // 해당 번호의 글이 없는 경우 null반환
    }

    // 관리자페이지 고객센터 메뉴 자주묻는질문 리스트 출력
    public PageResponseDTO FaqAdminSelect(PageRequestDTO pageRequestDTO,String faqCate,String faqType) {
        Pageable pageable = pageRequestDTO.getPageable("FaqNo");
        log.info("faqCate {}", faqCate);
        log.info("faqType {}", faqType);
        Page<Faq> pageFaq;
        if(faqCate == null && faqType == null){
            pageFaq = adminFaqRepository.findAllByOrderByFaqDateDesc(pageable);
        }else if(faqCate != null && faqType != null){
            pageFaq = adminFaqRepository.findByFaqCateAndFaqTypeOrderByFaqDateDesc(faqCate, faqType, pageable);
        }else{
            pageFaq = adminFaqRepository.findByFaqCateOrderByFaqDateDesc(faqCate,pageable);
        }

        List<FaqDTO> dtoList = pageFaq.getContent().stream()
                .map(faq -> modelMapper.map(faq, FaqDTO.class))
                .toList();

        log.info("pageNoticeChecked {}", pageFaq);

        int total = (int) pageFaq.getTotalElements();

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 관리자페이지 고객센터 메뉴 자주묻는질문 글 수정
    public void faqAdminUpdate(FaqDTO faqDTO) {

        // 게시글 번호
        int faqNo = faqDTO.getFaqNo();

        Faq faq = modelMapper.map(faqDTO, Faq.class);
        Faq savedFaq = adminFaqRepository.save(faq);
    }

    // 관리자페이지 고객센터 메뉴 자주묻는질문 글 삭제
    public void faqAdminDelete(int faqNo) {
        adminFaqRepository.deleteById(faqNo);
    }

    // faq 게시글 선택 삭제
    public ResponseEntity<?> deleteFaq(int[] faq_faqNos) {
        log.info("1" + Arrays.toString(faq_faqNos));

        boolean result = adminFaqRepository.deleteAdminCs(faq_faqNos);
        log.info("2" + result);
        Map<String, String> response = new HashMap<>();
        if(result){
            response.put("data","삭제 성공");
            log.info(response.toString());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            response.put("data","삭제 실패");
            log.info(response.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
