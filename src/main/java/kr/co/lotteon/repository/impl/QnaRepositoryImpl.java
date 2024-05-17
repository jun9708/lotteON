package kr.co.lotteon.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.entity.QQna;
import kr.co.lotteon.entity.Qna;
import kr.co.lotteon.repository.custom.QnaRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class QnaRepositoryImpl implements QnaRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QQna qQna = QQna.qna;


    public Page<Qna> selectMyQna(String userId, PageRequestDTO pageRequestDTO, Pageable pageable){

        //user qnd 조회
        List<Qna> selectMyQna = jpaQueryFactory
                                    .selectFrom(qQna)
                                    .where(qQna.userId.eq(userId))
                                    .orderBy(qQna.qnaDate.desc())
                                    .offset(pageable.getOffset())
                                    .limit(pageable.getPageSize())
                                    .fetch();

        long total = jpaQueryFactory
                .select(qQna.count())
                .from(qQna)
                .where(qQna.userId.eq(userId))
                .fetchOne();

        // 페이징 처리
        return new PageImpl<>(selectMyQna, pageable, total);
    }

}
