package kr.co.lotteon.service.admin;

import kr.co.lotteon.dto.NoticeDTO;
import kr.co.lotteon.dto.QnaDTO;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.entity.Notice;
import kr.co.lotteon.entity.Qna;
import kr.co.lotteon.repository.admin.AdminQnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminQnaService {

    private final AdminQnaRepository adminQnaRepository;
    private final ModelMapper modelMapper;

    // admin cs qna 리스트
    public PageResponseDTO qnaAdminSelect(PageRequestDTO pageRequestDTO,String qnaCate,String qnaType){
        Pageable pageable = pageRequestDTO.getPageable("qnaNo");

        Page<Qna> pageQna;
        if(qnaCate == null && qnaType == null){
            pageQna = adminQnaRepository.findAllByOrderByQnaDateDesc(pageable);
        }else if(qnaCate != null && qnaType != null){
            pageQna = adminQnaRepository.findByQnaCateAndQnaTypeOrderByQnaDateDesc(qnaCate, qnaType, pageable);
        }else{
            pageQna = adminQnaRepository.findByQnaCateOrderByQnaDateDesc(qnaCate, pageable);
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

    // admin cs qna 글보기
    public QnaDTO QnaAdminView(int qnaNo){

        // .orElse(null); optional 객체가 비어있을경우 대비
        Qna qna = adminQnaRepository.findById(qnaNo).orElse(null);
        log.info("특정 글 불러오기 modify {}",qna);
        if(qna !=null){
            // DTO로 변환후에 반환
            return modelMapper.map(qna, QnaDTO.class);
        }
        return null; // 해당 번호의 글이 없는 경우 null반환
    }

    // admin cs qna 답변
    public void qnaAdminReply(QnaDTO qnaDTO){

        // 게시글 번호
        int qnaNo = qnaDTO.getQnaNo();

        Qna qna = modelMapper.map(qnaDTO, Qna.class);
        Qna savedQna = adminQnaRepository.save(qna);
        log.info("savedQna 체크 {}", savedQna);
    }

    // admin cs qna 글삭제
    public void qnaAdminDelete(int qnaNo){
        adminQnaRepository.deleteById(qnaNo);
    }
}
