package kr.co.lotteon.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.prodOptDetailDTO;
import kr.co.lotteon.dto.ProdOptionDTO;
import kr.co.lotteon.dto.ResponseOptionDTO;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.custom.ProdOptionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.util.*;


@Slf4j
@RequiredArgsConstructor
public class ProdOptionRepositoryImpl implements ProdOptionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QProdOption qProdOption = QProdOption.prodOption;
    private final QProdOptDetail qProdOptDetail = QProdOptDetail.prodOptDetail;
    private final ModelMapper modelMapper;

    // 상품 옵션 조회
    @Override
    public ResponseOptionDTO selectProductOption(int prodNo){
        // 옵션 이름 조회
        List<String> optionName = jpaQueryFactory
                                    .select(qProdOption.optName)
                                    .from(qProdOption)
                                    .where(qProdOption.prodNo.eq(prodNo))
                                    .groupBy(qProdOption.optName)
                                    .fetch();

        // 옵션 값 조회
        LinkedHashMap<String, List<ProdOptionDTO>> optionMap = new LinkedHashMap<>();
        for (String optName : optionName) {
            List<ProdOption> optionValue = jpaQueryFactory
                                        .selectFrom(qProdOption)
                                        .where(
                                                qProdOption.prodNo.eq(prodNo)
                                                .and(qProdOption.optName.eq(optName))
                                        )
                                        .fetch();
            log.info("optionValue : " + optionValue);
            List<ProdOptionDTO> prodOptionDTOs = new ArrayList<>();
            for (ProdOption value : optionValue) {
                ProdOptionDTO prodOptionDTO = modelMapper.map(value, ProdOptionDTO.class);
                prodOptionDTOs.add(prodOptionDTO);
            }
            optionMap.put(optName, prodOptionDTOs);
            log.info("optionMap : " + optionMap);
        }

        // 해당 상품의 옵션별 재고 조회
        List<ProdOptDetail> optionDetail = jpaQueryFactory
                                            .selectFrom(qProdOptDetail)
                                            .where(qProdOptDetail.prodNo.eq(prodNo))
                                            .fetch();


        List<prodOptDetailDTO> prodOptDetailDTOS = new ArrayList<>();
        for (ProdOptDetail detail : optionDetail){
            prodOptDetailDTO prodOptDetailDTO = modelMapper.map(detail, prodOptDetailDTO.class);
            prodOptDetailDTOS.add(prodOptDetailDTO);
        }

        ResponseOptionDTO responseOptionDTO = new ResponseOptionDTO();
        responseOptionDTO.setOptionMap(optionMap);
        responseOptionDTO.setProdOptDetailDTOS(prodOptDetailDTOS);

        log.info("optionMap : " + optionMap);
        log.info("prodOptDetailDTO : " + prodOptDetailDTOS.toString());
        log.info("prodOptDetailDTO : " + responseOptionDTO.toString());

        return responseOptionDTO;
    }

}
