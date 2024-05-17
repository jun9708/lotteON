package kr.co.lotteon.service.seller;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.Notice;
import kr.co.lotteon.entity.ProdQna;
import kr.co.lotteon.entity.ProdQnaNote;
import kr.co.lotteon.repository.NoticeRepository;
import kr.co.lotteon.repository.ProdQnaNoteRepository;
import kr.co.lotteon.repository.ProdQnaRepository;
import kr.co.lotteon.repository.admin.AdminNoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SellerQnaService {

    private final ProdQnaRepository prodQnaRepository;
    private final ProdQnaNoteRepository prodQnaNoteRepository;
    private final NoticeRepository noticeRepository;
    private final AdminNoticeRepository adminNoticeRepository;
    private final ModelMapper modelMapper;


    // 판매자 관리페이지 - QNA List - 목록 조회
    public PageResponseDTO selectSellerQnaList(String prodSeller, PageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable("prodNo");
        Page<Tuple> selectProdList = prodQnaRepository.selectSellerQnaList(prodSeller, pageRequestDTO, pageable);

        List<ProdQnaDTO> prodQnaList = selectProdList.getContent().stream()
                .map(tuple -> {
                    ProdQna prodQna = tuple.get(0, ProdQna.class);
                    String prodName = tuple.get(1, String.class);
                    ProdQnaDTO prodQnaDTO = modelMapper.map(prodQna, ProdQnaDTO.class);
                    prodQnaDTO.setProdName(prodName);
                    return prodQnaDTO;
                })
                .toList();
        int total = (int) selectProdList.getTotalElements();

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(prodQnaList)
                .total(total)
                .build();
    }

    // 판매자 관리페이지 - QNA View
    public Map<String, Object> selectSellerQnaView(int qnaNo) {
        return prodQnaRepository.selectSellerQnaView(qnaNo);
    }

    // 판매자 관리페이지 - QNA View - 상품 문의 답글 등록
    public ResponseEntity<?> insertQnaNote(ProdQnaNoteDTO prodQnaNoteDTO){
        ProdQnaNote prodQnaNote = modelMapper.map(prodQnaNoteDTO, ProdQnaNote.class);
        ProdQnaNote saveQnaNote = prodQnaNoteRepository.save(prodQnaNote);
        Optional<ProdQna> optProdQna = prodQnaRepository.findById(prodQnaNoteDTO.getProdQnaNo());
        if (optProdQna.isPresent()){
            optProdQna.get().setProdQnaStatus("답변 등록");
            prodQnaRepository.save(optProdQna.get());
        }
        if (saveQnaNote.getContent() != null) {
            return ResponseEntity.ok().body(saveQnaNote.getCQnaNo());
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(saveQnaNote.getCQnaNo());
        }
    }

    // 판매자 관리페이지 - QNA View - 상품 문의 답글 삭제
    public ResponseEntity<?> deleteQnaNote(String CQnaNo) {
        Optional<ProdQnaNote> optQnaNote = prodQnaNoteRepository.findById(Integer.parseInt(CQnaNo));
        if (optQnaNote.isPresent()) {
            prodQnaNoteRepository.deleteById(Integer.parseInt(CQnaNo));
        }

        Optional<ProdQnaNote> resultQnaNote = prodQnaNoteRepository.findById(Integer.parseInt(CQnaNo));
        if (resultQnaNote.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }
    }

    // 판매자 관리페이지 - QNA View - 상품 문의 답글 수정
    public ResponseEntity<?> modifyQnaNote(ProdQnaNoteDTO prodQnaNoteDTO){
        log.info("prodQnaNoteDTO2 : " + prodQnaNoteDTO);
        Optional<ProdQnaNote> optProdQnaNote = prodQnaNoteRepository.findById(prodQnaNoteDTO.getCQnaNo());
        log.info("optProdQnaNote : " + optProdQnaNote.toString());
        if (optProdQnaNote.isPresent()){
            optProdQnaNote.get().setCQnaDate(prodQnaNoteDTO.getCQnaDate());
            optProdQnaNote.get().setContent(prodQnaNoteDTO.getContent());
            prodQnaNoteRepository.save(optProdQnaNote.get());
            return ResponseEntity.ok().body(1);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
    }

    // 판매자 관리페이지 - NOTICE List - 목록조회
    public PageResponseDTO selectSellerNoticeList(PageRequestDTO pageRequestDTO, String noticeCate,String noticeType) {
        Pageable pageable = pageRequestDTO.getPageable("noticeNo");

        Page<Notice> pageNotice = null;
        if (noticeCate == null){
            pageNotice = noticeRepository.findAllByOrderByNoticeDateDesc(pageable);
        }else if(noticeCate != null && noticeType == null) {
            pageNotice = adminNoticeRepository.findByNoticeCateOrderByNoticeDateDesc(noticeCate, pageable);
        }else if(noticeCate != null && noticeType != null){
            pageNotice = adminNoticeRepository.findByNoticeCateAndNoticeTypeOrderByNoticeDateDesc(noticeCate,noticeType,pageable);
        }

        List<NoticeDTO> dtoList = pageNotice.getContent().stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class))
                .toList();

        int total = (int) pageNotice.getTotalElements();
        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 판매자 관리페이지 - NOTICE view
    @Transactional
    public NoticeDTO selectSellerNoticeView(int noticeNo){

        // .orElse(null); optional 객체가 비어있을경우 대비
        Notice notice = noticeRepository.findById(noticeNo).orElse(null);
        log.info("특정 글 불러오기 modify {}",notice);
        if(notice !=null){
            // DTO로 변환후에 반환
            adminNoticeRepository.incrementHitByNoticeNo(noticeNo);
            log.info("noticeNo1 {}", noticeNo);
            return modelMapper.map(notice, NoticeDTO.class);
        }
        return null; // 해당 번호의 글이 없는 경우 null반환
    }
}
