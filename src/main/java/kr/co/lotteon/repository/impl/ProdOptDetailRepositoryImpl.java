package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.ProdOptionDTO;
import kr.co.lotteon.dto.ResponseOptionDTO;
import kr.co.lotteon.dto.prodOptDetailDTO;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.custom.ProdOptDetailRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class ProdOptDetailRepositoryImpl implements ProdOptDetailRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QProdOption qProdOption = QProdOption.prodOption;
    private final QProdOptDetail qProdOptDetail = QProdOptDetail.prodOptDetail;
    private final ModelMapper modelMapper;
    // select * from `prodOptDetail` where optDetailNo = ?
    // select * from `prodOtion` where optNo = ?
    @Override
    public ProdOptDetail selectOptDetailWihtName(int optNo){

        ProdOptDetail selectedOpt = jpaQueryFactory
                .select(qProdOptDetail)
                .from(qProdOptDetail)
                .where(qProdOptDetail.optDetailNo.eq(optNo))
                .fetchOne();

        if (selectedOpt != null) {
            //log.info(selectedOpt.toString());

            selectedOpt.setOptValue1(getOptionValue(selectedOpt.getOptNo1()));
            selectedOpt.setOptValue2(getOptionValue(selectedOpt.getOptNo2()));
            selectedOpt.setOptValue3(getOptionValue(selectedOpt.getOptNo3()));

            //log.info("optValue1: " + selectedOpt.getOptValue1());
            //log.info("optValue2: " + selectedOpt.getOptValue2());
            //log.info("optValue3: " + selectedOpt.getOptValue3());
        }else {
            log.info("ProdOptDetail not found for optNo: " + optNo);
        }
        return selectedOpt;
    }

    private String getOptionValue(int optNo) {
        return jpaQueryFactory
                .select(qProdOption.optValue)
                .from(qProdOption)
                .where(qProdOption.optNo.eq(optNo))
                .fetchOne();
    }

}
