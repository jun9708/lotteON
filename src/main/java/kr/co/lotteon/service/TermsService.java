package kr.co.lotteon.service;

import kr.co.lotteon.dto.TermsDTO;
import kr.co.lotteon.mapper.TermsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TermsService {

    private final TermsMapper termsMapper;

    //이용약관 동의 출력
    public TermsDTO selectTerms(){
        int termsNo = 2;
        return termsMapper.selectTerms(termsNo);
    }

    // 이용약관 페이지의 카테고리별 약관 조회
    public List<TermsDTO> selectTermsForPolicy(String cate) {

        int termsNo = 1;
        String termsText = termsMapper.selectTermsForPolicy(cate, termsNo);
        String[] termSplit = termsText.split("wkfmfrjdpdy");

        List<TermsDTO> termsDTOList = new ArrayList<>();
        for (int i=1 ; i < termSplit.length ; i++) {
            TermsDTO termsDTO = new TermsDTO();
            String[] eachTerm = termSplit[i].split("wpahrdldpdy");
            termsDTO.setTermHead(termSplit[0]);
            termsDTO.setTermTitle(eachTerm[0]);
            termsDTO.setTermContent(eachTerm[1]);
            termsDTOList.add(termsDTO);
        }
        return termsDTOList;
    }

}
