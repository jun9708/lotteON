package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.dto.PageResponseDTO;
import kr.co.lotteon.dto.ProdQnaDTO;
import kr.co.lotteon.dto.ProdQnaNoteDTO;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.custom.ProdQnaRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class ProdQnaRepositoryImpl implements ProdQnaRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QProdQna qprodQna = QProdQna.prodQna;
    private final QProdQnaNote qProdQnaNote = QProdQnaNote.prodQnaNote;
    private final ModelMapper modelMapper;


    // 판매자 관리페이지 - QNA List - 목록 조회
    public Page<Tuple> selectSellerQnaList(String prodSeller, PageRequestDTO pageRequestDTO, Pageable pageable){

        // 전체 조회
        if (pageRequestDTO.getKeyword() == null){

            QueryResults<Tuple> selectQnaList = jpaQueryFactory
                                            .select(qprodQna, qProduct.prodName)
                                            .from(qprodQna)
                                            .join(qProduct)
                                            .on(qprodQna.prodNo.eq(qProduct.prodNo))
                                            .where(qProduct.prodSeller.eq(prodSeller))
                                            .orderBy(qprodQna.prodQnaDate.desc())
                                            .offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .fetchResults();

            log.info("selectQnaList : " + selectQnaList.toString());

            List<Tuple> qnaList = selectQnaList.getResults();
            int total = (int)selectQnaList.getTotal();
            return new PageImpl<>(qnaList, pageable, total);

        }else {
            // 검색 조회
            String type = pageRequestDTO.getType();
            String keyword = pageRequestDTO.getKeyword();
            BooleanExpression expression = null;

            // 검색 종류에 따른 where절 표현식 생성
            if(type.equals("prodNo")){
                expression = qprodQna.prodNo.eq(Integer.parseInt(keyword));

            }else if(type.equals("prodQnaTitle")){
                expression = qprodQna.prodQnaTitle.contains(keyword);

            }else if(type.equals("prodQnaStatus")){
                expression = qprodQna.prodQnaStatus.contains(keyword);
            }

            QueryResults<Tuple> selectQnaList = jpaQueryFactory
                    .select(qprodQna, qProduct.prodName)
                    .from(qprodQna)
                    .join(qProduct)
                    .on(qprodQna.prodNo.eq(qProduct.prodNo))
                    .where(qProduct.prodSeller.eq(prodSeller))
                    .where(expression)
                    .orderBy(qprodQna.prodQnaDate.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            List<Tuple> qnaList = selectQnaList.getResults();
            int total = (int)selectQnaList.getTotal();
            return new PageImpl<>(qnaList, pageable, total);
        }
    }

    // 판매자 관리페이지 - QNA View
    public Map<String, Object> selectSellerQnaView(int qnaNo){
        // 상품 문의 게시글 조회
        QueryResults<Tuple> selectQna = jpaQueryFactory
                                .select(qprodQna, qProduct.prodName)
                                .from(qprodQna)
                                .join(qProduct)
                                .on(qprodQna.prodNo.eq(qProduct.prodNo))
                                .where(qprodQna.prodQnaNo.eq(qnaNo))
                                .fetchResults();

        ProdQnaDTO prodQnaDTO = selectQna.getResults().stream()
                .map(tuple -> {
                    ProdQna prodQna = tuple.get(0, ProdQna.class);
                    String prodName = tuple.get(1, String.class);
                    ProdQnaDTO resultQna = modelMapper.map(prodQna, ProdQnaDTO.class);
                    resultQna.setProdName(prodName);
                    return resultQna;
                })
                .findFirst().orElse(null);

        // 상품 문의 게시글 답변 조회
        List<ProdQnaNote> selectQnaNote = jpaQueryFactory
                                .selectFrom(qProdQnaNote)
                                .where(qProdQnaNote.prodQnaNo.eq(prodQnaDTO.getProdQnaNo()))
                                .fetch();
        List<ProdQnaNoteDTO> prodQnaNoteList = new ArrayList<>();
        for (ProdQnaNote qnaNote : selectQnaNote){
            ProdQnaNoteDTO prodQnaNoteDTO = modelMapper.map(qnaNote, ProdQnaNoteDTO.class);
            prodQnaNoteList.add(prodQnaNoteDTO);
        }

        Map<String, Object> resultQna = new HashMap<>();
        resultQna.put("prodQnaDTO", prodQnaDTO);
        resultQna.put("prodQnaNoteList", prodQnaNoteList);
        return resultQna;
    }

    // 관리자 메인페이지 - 고객문의 최신순 5개 조회
    // 판매자 메인페이지 - 고객문의 최신순 5개 조회
    public List<ProdQna> selectProdQnaForIndex(String prodSeller) {
        if(prodSeller.equals("ADMIN")) {
             return jpaQueryFactory
                    .selectFrom(qprodQna)
                    .orderBy(qprodQna.prodQnaDate.desc())
                    .limit(5)
                    .fetch();
        }else {
            return jpaQueryFactory
                    .select(qprodQna)
                    .from(qprodQna)
                    .join(qProduct)
                    .on(qprodQna.prodNo.eq(qProduct.prodNo))
                    .where(qProduct.prodSeller.eq(prodSeller))
                    .orderBy(qprodQna.prodQnaDate.desc())
                    .limit(5)
                    .fetch();
        }
    }

    @Override
    public Page<ProdQna> selectMyProdQna(String userId, PageRequestDTO pageRequestDTO, Pageable pageable) {

        //user qnd 조회
        List<ProdQna> selectMyQna = jpaQueryFactory
                .selectFrom(qprodQna)
                .where(qprodQna.userId.eq(userId))
                .orderBy(qprodQna.prodQnaDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(qprodQna.count())
                .from(qprodQna)
                .where(qprodQna.userId.eq(userId))
                .fetchOne();

        // 페이징 처리
        return new PageImpl<>(selectMyQna, pageable, total);

    }

    // 상품 문의 조회
    public PageResponseDTO selectProdQna(int prodNo, Pageable pageable, PageRequestDTO pageRequestDTO) {
        log.info("pageable : " + pageable);
        List<ProdQna> results = jpaQueryFactory
                .selectFrom(qprodQna)
                .where(qprodQna.prodNo.eq(prodNo))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(qprodQna)
                .where(qprodQna.prodNo.eq(prodNo)).fetchCount();

        log.info("results : " + results);

        List<ProdQnaDTO> prodQnaDTOList = new ArrayList<>();
        for (ProdQna each : results) {
            List<ProdQnaNote> prodQnaNotes = jpaQueryFactory
                    .selectFrom(qProdQnaNote)
                    .where(qProdQnaNote.prodQnaNo.eq(each.getProdQnaNo()))
                    .fetch();
            log.info("prodQnaNotes : " + prodQnaNotes);
            ProdQnaDTO prodQnaDTO = modelMapper.map(each, ProdQnaDTO.class);
            List<ProdQnaNoteDTO> prodQnaNoteDTOList = new ArrayList<>();
            for (ProdQnaNote eachNote : prodQnaNotes) {
                prodQnaNoteDTOList.add(modelMapper.map(eachNote, ProdQnaNoteDTO.class));
                log.info("prodQnaNoteDTOList : " + prodQnaNoteDTOList);
            }
            prodQnaDTO.setProdQnaNoteList(prodQnaNoteDTOList);
            prodQnaDTOList.add(prodQnaDTO);
            log.info("prodQnaDTO : " + prodQnaDTO);
            log.info("prodQnaDTOList : " + prodQnaDTOList);
        }

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(prodQnaDTOList)
                .total((int)total)
                .build();



    }
}
