package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.PageRequestDTO;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.custom.UserRepositoryCustom;
import kr.co.lotteon.repository.custom.WishRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class WishRepositoryImpl implements WishRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QWish qWish = QWish.wish;
    private final QProduct qProduct = QProduct.product;
    private final QProductimg qProductimg = QProductimg.productimg;
    private final QProdOption qProdOption = QProdOption.prodOption;
    private final QProdOptDetail qProdOptDetail = QProdOptDetail.prodOptDetail;

    // my - wish 조회
    public Page<Tuple> selectUserWish(String userId, PageRequestDTO pageRequestDTO, Pageable pageable) {


        QueryResults<Tuple> resultWish = jpaQueryFactory
                .select(qWish, qProduct.prodName, qProduct.prodPrice, qProductimg.thumb190, qProduct.cateCode)
                .from(qWish)
                .join(qProduct)
                .on(qWish.prodNo.eq(qProduct.prodNo))
                .join(qProductimg)
                .on(qWish.prodNo.eq(qProductimg.prodNo))
                .where(qWish.userId.eq(userId))
                .orderBy(qWish.wishRdate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<Tuple> tupleList = resultWish.getResults();
        int total = (int) resultWish.getTotal();
        log.info("tupleList : " + tupleList);
        return new PageImpl<>(tupleList, pageable, total);
    }

    // 상품 옵션 이름 조회
    public String selectProdOption(int optDetailNo){
        ProdOptDetail prodOptDetail = jpaQueryFactory
                .selectFrom(qProdOptDetail)
                .where(qProdOptDetail.optDetailNo.eq(optDetailNo))
                .fetchOne();

        String optName = "";
        if (prodOptDetail.getOptNo1() != 0) {
            optName += jpaQueryFactory
                    .select(qProdOption.optValue)
                    .from(qProdOption)
                    .where(qProdOption.optNo.eq(prodOptDetail.getOptNo1()))
                    .fetchOne();
        }
        if (prodOptDetail.getOptNo2() != 0) {
            optName += "_" + jpaQueryFactory
                    .select(qProdOption.optValue)
                    .from(qProdOption)
                    .where(qProdOption.optNo.eq(prodOptDetail.getOptNo2()))
                    .fetchOne();
        }
        if (prodOptDetail.getOptNo3() != 0) {
            optName += "_" + jpaQueryFactory
                    .select(qProdOption.optValue)
                    .from(qProdOption)
                    .where(qProdOption.optNo.eq(prodOptDetail.getOptNo3()))
                    .fetchOne();
        }
        return optName;
    }

}