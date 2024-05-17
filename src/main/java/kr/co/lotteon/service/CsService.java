package kr.co.lotteon.service;

import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.Faq;
import kr.co.lotteon.entity.Notice;
import kr.co.lotteon.entity.Qna;
import kr.co.lotteon.repository.FaqRepository;
import kr.co.lotteon.repository.NoticeRepository;
import kr.co.lotteon.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CsService {

    private final QnaRepository qnaRepository;
    private final NoticeRepository noticeRepository;
    private final FaqRepository faqRepository;
    private final ModelMapper modelMapper;

    // notice 인덱스페이지  리스트
    public List<Notice> noticeList(){
        return noticeRepository.findAllByOrderByNoticeDateDesc();
    }

    // notice page 리스트 출력
    public PageResponseDTO selectNoticePages(PageRequestDTO pageRequestDTO,String noticeCate){
        Pageable pageable = pageRequestDTO.getPageable("noticeNo");

        Page<Notice> pageNotice;
        if(noticeCate == null){
            pageNotice = noticeRepository.findAllByOrderByNoticeDateDesc(pageable);
        }else{
            pageNotice = noticeRepository.findByNoticeCateOrderByNoticeDateDesc(noticeCate, pageable);
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
    // 관리자페이지 고객센터 메뉴 공지사항 특정 글 불러오기
    public NoticeDTO noticeView(int noticeNo){

        // .orElse(null); optional 객체가 비어있을경우 대비
        Notice notice = noticeRepository.findById(noticeNo).orElse(null);
        log.info("특정 글 불러오기 view {}",notice);
        if(notice !=null){
            // DTO로 변환후에 반환
            return modelMapper.map(notice, NoticeDTO.class);
        }
        return null; // 해당 번호의 글이 없는 경우 null반환
    }
    // 고객센터 qna 등록
    public void insertQna(QnaDTO qnaDTO){

        // qnaDTO를 Qna엔티티로 변환
        Qna qna = modelMapper.map(qnaDTO, Qna.class);
        log.info("qna ={}", qna.toString());
        // 매핑된 Notice 엔티티를 DB에 저장
        Qna savedQna = qnaRepository.save(qna);
        log.info("savedQna ={}", savedQna.toString());
    }

    //  qna 인덱스페이지 리스트
    public List<Qna> qnaList(){
        return qnaRepository.findAllByOrderByQnaDateDesc();
    }

    // qna page 리스트 출력
    public PageResponseDTO selectQnaPages(PageRequestDTO pageRequestDTO,String qnaCate){
        Pageable pageable = pageRequestDTO.getPageable("qnaNo");
        Page<Qna> pageQna;
        if(qnaCate == null){
            pageQna = qnaRepository.findAll(pageable);
        }else{
            pageQna = qnaRepository.findByQnaCate(qnaCate,pageable);
        }

        List<QnaDTO> dtoList = pageQna.getContent().stream()
                .map(qna -> modelMapper.map(qna, QnaDTO.class))
                .toList();

        int total = (int) pageQna.getTotalElements();

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // qna view 불러오기
    public QnaDTO qnaView(int qnaNo){

        // .orElse(null); optional 객체가 비어있을경우 대비
        Qna qna = qnaRepository.findById(qnaNo).orElse(null);
        log.info("특정 글 불러오기 view {}",qna);
        if(qna !=null){
            // DTO로 변환후에 반환
            return modelMapper.map(qna, QnaDTO.class);
        }
        return null; // 해당 번호의 글이 없는 경우 null반환
    }

    // faq 리스트
    public Map<String, List<FaqDTO>> faqList(String faqCate){
        if(faqCate == null){
            return null;
        }else{
            List<Faq> faqList = faqRepository.findByFaqCate(faqCate); // type 구분 없음

            Map<String, List<FaqDTO>> faqMap = new HashMap<>(); // 비어있는 Map 생성(반환객체)
            for (Faq each : faqList){
                // Entity -> DTO
                FaqDTO eachDTO = modelMapper.map(each, FaqDTO.class);
                // faqMap에 타입들을 비교 , containKey() = 비교
                if (faqMap.containsKey(eachDTO.getFaqType())){
                    // 같은 값이 있을때 가지고있는 값을 불러와서 새로운 값(eachDTO)를 add
                    List<FaqDTO> existingFaq = faqMap.get(eachDTO.getFaqType());
                    existingFaq.add(eachDTO);
                }else {
                    /*
                        -같은 값이 없는 경우-
                        새로운 리스트(newFaq)를 생성해서 eachDTO를 newFaq에 add한 후에
                        newFaq를 eachDTO로 put
                     */
                    List<FaqDTO> newFaq = new ArrayList<>();
                    newFaq.add(eachDTO);
                    faqMap.put(eachDTO.getFaqType(), newFaq);
                }
            }
            return faqMap;
        }
    }

//    // faq view 불러오기
    public FaqDTO faqView(int faqNo){

        // .orElse(null); optional 객체가 비어있을경우 대비
        Faq faq = faqRepository.findById(faqNo).orElse(null);

        log.info("특정 글 불러오기 view {}",faq);
        if(faq !=null){
            // DTO로 변환후에 반환
            return modelMapper.map(faq, FaqDTO.class);
        }
        return null; // 해당 번호의 글이 없는 경우 null반환
    }
    //faq view
    public FaqDTO faqViewCate(String faqCate){

        List<Faq> byFaqCate = faqRepository.findByFaqCate(faqCate);
        if(byFaqCate != null){
            return modelMapper.map(byFaqCate, FaqDTO.class);
        }
        return null;
    }
}
