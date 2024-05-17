package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.ProductPageRequestDTO;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.custom.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QProductimg qProductimg = QProductimg.productimg;
    private final QProdOption qProdOption = QProdOption.prodOption;
    private final QSeller qSeller = QSeller.seller;


    //ADMIN 페이지 프로덕트 조회
    @Override
    public Page<Tuple> selectProducts(ProductPageRequestDTO pageRequestDTO, Pageable pageable) {

        QueryResults<Tuple> results = jpaQueryFactory
                .select(qProduct, qProductimg)
                .from(qProduct)
                .join(qProductimg)
                .on(qProduct.prodNo.eq(qProductimg.prodNo))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProduct.prodNo.desc())
                .fetchResults();

        List<Tuple> content = results.getResults();
        log.info(content.toString());
        long total = results.getTotal();
        log.info("total : {}", total);
        return new PageImpl<>(content, pageable, total);
    }
    // Index 페이지 상품 조회
    @Override
    public List<Tuple> selectIndexProducts(String sort){

        OrderSpecifier<?> orderSpecifier = null;
        int limitCount = 8;

        if (sort != null && sort.startsWith("prodDiscount")){
            orderSpecifier = qProduct.prodDiscount.desc();
        }else if (sort != null && sort.startsWith("prodSold")) {
            orderSpecifier = qProduct.prodSold.desc();
            limitCount = 5;
        }else if (sort != null && sort.startsWith("prodRdate")) {
            orderSpecifier = qProduct.prodRdate.desc();
        }else if (sort != null && sort.startsWith("prodScore")) {
            orderSpecifier = qProduct.tReviewScore.desc();
        }else if (sort != null && sort.startsWith("prodHit")) {
            orderSpecifier = qProduct.prodHit.desc();
        }else {
            orderSpecifier = qProduct.prodSold.desc();
        }


        return jpaQueryFactory
                .select(qProduct, qProductimg)
                .from(qProduct)
                .join(qProductimg)
                .on(qProduct.prodNo.eq(qProductimg.prodNo))
                .orderBy(orderSpecifier)
                .limit(limitCount)
                .fetchResults()
                .getResults();

    }

    // cate 로 프로덕트 조회
    @Override
    public Page<Tuple> selectProductsByCate(ProductPageRequestDTO pageRequestDTO, Pageable pageable){
        String sort = pageRequestDTO.getSort();
        String seletedCate = pageRequestDTO.getCateCode();
        OrderSpecifier<?> orderSpecifier = null;
        log.info("here1 : " + sort);

        if (sort != null && sort.startsWith("prodSold")){
            orderSpecifier = qProduct.prodSold.desc();
        }else if (sort != null && sort.startsWith("prodLowPrice")) {
            orderSpecifier = qProduct.prodPrice.asc();
        }else if (sort != null && sort.startsWith("prodHighPrice")) {
            orderSpecifier = qProduct.prodPrice.desc();
        }else if (sort != null && sort.startsWith("prodScore")) {
            orderSpecifier = qProduct.tReviewScore.desc();
        }else if (sort != null && sort.startsWith("prodReview")) {
            orderSpecifier = qProduct.tReviewCount.desc();
        }else if (sort != null && sort.startsWith("prodRdate")) {
            orderSpecifier = qProduct.prodRdate.desc();
        }else if (sort != null && sort.startsWith("prodHit")) {
            orderSpecifier = qProduct.prodHit.desc();
        }else if (sort != null && sort.startsWith("prodDiscount")){
            orderSpecifier = qProduct.prodDiscount.desc();
        }else {
            orderSpecifier = qProduct.prodSold.desc();
        }

        QueryResults<Tuple> results = jpaQueryFactory
                .select(qProduct, qProductimg)
                .from(qProduct)
                .join(qProductimg)
                .on(qProduct.prodNo.eq(qProductimg.prodNo))
                .where(qProduct.cateCode.like(seletedCate+"%"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetchResults();

        List<Tuple> content = results.getResults();
        log.info(content.toString());
        long total = results.getTotal();
        log.info("total : {}", total);
        return new PageImpl<>(content, pageable, total);
    }

    public Page<Tuple> searchProductsByCateAndKeyWord(ProductPageRequestDTO pageRequestDTO, Pageable pageable){

        String sort = pageRequestDTO.getSort();
        String seletedCate = pageRequestDTO.getCateCode();
        OrderSpecifier<?> orderSpecifier = null;
        log.info("here1 : " + sort);

        if (sort != null && sort.startsWith("prodSold")){
            orderSpecifier = qProduct.prodSold.desc();
        }else if (sort != null && sort.startsWith("prodLowPrice")) {
            orderSpecifier = qProduct.prodPrice.asc();
        }else if (sort != null && sort.startsWith("prodHighPrice")) {
            orderSpecifier = qProduct.prodPrice.desc();
        }else if (sort != null && sort.startsWith("prodScore")) {
            orderSpecifier = qProduct.tReviewScore.desc();
        }else if (sort != null && sort.startsWith("prodReview")) {
            orderSpecifier = qProduct.tReviewCount.desc();
        }else if (sort != null && sort.startsWith("prodRdate")) {
            orderSpecifier = qProduct.prodRdate.desc();
        }else if (sort != null && sort.startsWith("prodHit")) {
            orderSpecifier = qProduct.prodHit.desc();
        }else if (sort != null && sort.startsWith("prodDiscount")){
            orderSpecifier = qProduct.prodDiscount.desc();
        }else {
            orderSpecifier = qProduct.prodSold.desc();
        }

        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();

        log.info("keyword : " + keyword);
        log.info("type : " + type);

        BooleanExpression expression = null;
        List<BooleanExpression> expressions = new ArrayList<>();

        String[] types = null;
        if (type!=null){
            types = type.split(",");
        }

        if (types != null){
            for (String t : types){
                try {
                    // type이 "prodName"인 경우 조건 추가
                    if ("prodName".equals(t)) {
                        expressions.add(qProduct.prodName.contains(keyword));
                    }
                    // type이 "prodInfo"인 경우 조건 추가
                    if ("prodInfo".equals(t)) {
                        expressions.add(qProduct.prodInfo.contains(keyword));
                    }
                    // type이 "prodPrice"인 경우 조건 추가
                    if ("prodPrice".equals(t)) {
                        int min = Integer.parseInt(pageRequestDTO.getMin());
                        int max = Integer.parseInt(pageRequestDTO.getMax());
                        expressions.add(qProduct.prodPrice.between(min, max));
                    }
                } catch (Exception e) {
                    // 예외 발생 시에도 기본적으로 "prodName"에 대한 조건을 적용하도록 함
                    expressions.add(qProduct.prodName.contains(keyword));
                }
            }
            if (!expressions.isEmpty()) {
                expression = expressions.stream().reduce(BooleanExpression::and).orElse(null);
            }
        }else {
            expression = qProduct.prodName.contains(keyword);
        }


        QueryResults<Tuple> results = jpaQueryFactory
                .select(qProduct, qProductimg, qSeller)
                .from(qProduct)
                .join(qProductimg)
                .on(qProduct.prodNo.eq(qProductimg.prodNo))
                .join(qSeller)
                .on(qProduct.prodSeller.eq(qSeller.sellerNo))
                .where(expression.and(qProduct.cateCode.like(seletedCate+"%")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetchResults();

        List<Tuple> content = results.getResults();
        log.info(content.toString());
        long total = results.getTotal();
        log.info("total : {}", total);
        return new PageImpl<>(content, pageable, total);

    }

    // 제품 상세 조회
    @Override
    public Tuple selectProduct(int prodNo){
        return jpaQueryFactory
                .select(qProduct, qProductimg)
                .from(qProduct)
                .join(qProductimg)
                .on(qProduct.prodNo.eq(qProductimg.prodNo))
                .where(qProduct.prodNo.eq(prodNo))
                .fetchOne();
    }

    @Override
    public Tuple selectProductById(int prodNo){
        return jpaQueryFactory
                .select(qProduct, qProductimg)
                .from(qProduct)
                .join(qProductimg)
                .on(qProduct.prodNo.eq(qProductimg.prodNo))
                .where(qProduct.prodNo.eq(prodNo))
                .fetchOne();

    }
}