package kr.co.lotteon.mapper;

import kr.co.lotteon.dto.TermsDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TermsMapper {

    //이용약관 동의 출력
    public TermsDTO selectTerms(int termsNo);

    //이용약관 동의 출력
    public String selectTermsForPolicy(String cate, int termsNo);

}
