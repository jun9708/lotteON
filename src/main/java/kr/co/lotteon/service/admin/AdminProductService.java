package kr.co.lotteon.service.admin;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.ProdOptDetail;
import kr.co.lotteon.entity.ProdOption;
import kr.co.lotteon.entity.Product;
import kr.co.lotteon.entity.Productimg;
import kr.co.lotteon.repository.ProdOptDetailRepository;
import kr.co.lotteon.repository.ProdOptionRepository;
import kr.co.lotteon.repository.ProductRepository;
import kr.co.lotteon.repository.ProductimgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class AdminProductService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final ProductimgRepository productimgRepository;
    private final ProdOptionRepository optionRepository;
    private final ProdOptDetailRepository optDetailRepository;

    // 어드민 페이지 물건 조회 메서드 (+ 검색)
    public ProductPageResponseDTO selectProductsForAdmin(ProductPageRequestDTO productPageRequestDTO){

        Pageable pageable = productPageRequestDTO.getPageable("prodNo");

        Page<Tuple> tuplePage = productRepository.selectProducts(productPageRequestDTO, pageable);
        log.info("selectProduct!....:"+tuplePage.toString());
        List<ProductDTO> dtoList = tuplePage.getContent().stream()
                .map(tuple -> {
                    Product product = tuple.get(0, Product.class);
                    Productimg productImg = tuple.get(1, Productimg.class);

                    //Productimg에서 썸네일 정보를 가져와서 ProductDTO에 설정
                    if (productImg != null) {
                        product.setThumb190(productImg.getThumb190());
                        product.setThumb230(productImg.getThumb230());
                        product.setThumb456(productImg.getThumb456());
                        product.setThumb940(productImg.getThumb940());
                    }

                    return modelMapper.map(product, ProductDTO.class);
                })
                .toList();
        log.info("selectProducts!....:"+dtoList.toString());
        int total = (int) tuplePage.getTotalElements();
        return ProductPageResponseDTO.builder()
                .productPageRequestDTO(productPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 어드민 물건등록 기능
    public void registerProduct(ProductDTO productDTO, ProductimgDTO productimgDTO){

        Product product = modelMapper.map(productDTO, Product.class);

        Productimg uploadedImages = new Productimg();

        uploadedImages.setThumb190(fileUpload(productimgDTO.getMultThumb190(), "thumb190"));
        uploadedImages.setThumb230(fileUpload(productimgDTO.getMultThumb230(), "thumb230"));
        uploadedImages.setThumb456(fileUpload(productimgDTO.getMultThumb456(), "thumb456"));
        uploadedImages.setThumb940(fileUpload(productimgDTO.getMultThumb940(), "thumb940"));
        log.info(uploadedImages.getThumb190());

        Product savedProduct = productRepository.save(product);
        log.info("saved product: " + savedProduct);
        uploadedImages.setProdNo(savedProduct.getProdNo());
        log.info("saved product1.....: " + savedProduct.getProdNo());
        Productimg savedProductimg = productimgRepository.save(uploadedImages);
        log.info("registerProduct....."+savedProductimg.toString());


    }

    //이미지 업로드 메서드
    @Value("${file.upload.path}")
    private String fileUploadPath;
    public String fileUpload(MultipartFile images, String thumbnailSize){
        String path = new File(fileUploadPath).getAbsolutePath();

        if (!images.isEmpty()){
            String oName = images.getOriginalFilename();
            String ext = oName.substring(oName.lastIndexOf(".")); //확장자
            String sName = UUID.randomUUID().toString()+ ext;

            try {
                // 이미지를 메모리에 로드하여 썸네일 생성 후 저장하지 않고 경로 반환
                String thumbnailPath = mkThumbnail(path, sName, images, thumbnailSize);

                return thumbnailPath; // 썸네일 이미지 경로 반환
            } catch (IOException e) {
                log.error("Failed to create thumbnail: " + e.getMessage());
                return null;
            }
        }

        return null;
    }

    //썸네일 생성 메서드
    private String mkThumbnail(String path, String sName, MultipartFile images, String thumbnailSize) throws IOException {
        switch (thumbnailSize) {
            case "thumb190":
                Thumbnails.of(images.getInputStream())
                        .size(190, 190)
                        .toFile(new File(path, "thumb190_" + sName));
                return "thumb190_" + sName;
            case "thumb230":
                Thumbnails.of(images.getInputStream())
                        .size(230, 230)
                        .toFile(new File(path, "thumb230_" + sName));
                return "thumb230_" + sName;
            case "thumb456":
                Thumbnails.of(images.getInputStream())
                        .size(456, 456)
                        .toFile(new File(path, "thumb456_" + sName));
                return "thumb456_" + sName;
            case "thumb940":
                Thumbnails.of(images.getInputStream())
                        .width(940)
                        .toFile(new File(path, "thumb940_" + sName));
                return "thumb940_" + sName;
            default:
                return null;
        }
    }

    // 상품리스트에서 상품 수정으로 넘어갈때 상품 정보 조회
    public Map<String, Object> selectProductOption(int prodNo){
        // 상품 조회
        Optional<Product> optProduct = productRepository.findById(prodNo);
    
        ProductDTO productDTO = new ProductDTO();
        List<ProdOptionDTO> optionDTOList = new ArrayList<>();
        List<prodOptDetailDTO> prodOptDetailDTOList = new ArrayList<>();
        Productimg productimg = new Productimg();

        if (optProduct.isPresent()){
            // 옵션 조회
            productDTO = modelMapper.map(optProduct, ProductDTO.class);
            List<ProdOption> optionList = optionRepository.findByProdNo(optProduct.get().getProdNo());
            // 이미지 조회
            productimg = productimgRepository.findByProdNo(optProduct.get().getProdNo());
            if (!optionList.isEmpty()){
                for (ProdOption options : optionList) {
                    ProdOptionDTO optionDTO = modelMapper.map(options, ProdOptionDTO.class);
                    optionDTOList.add(optionDTO);
                }
            }
            // 옵션 detail 조회
            List<ProdOptDetail> prodOptDetails = optDetailRepository.findByProdNo(optProduct.get().getProdNo());
            if (!prodOptDetails.isEmpty()){
                for (ProdOptDetail optionDetail : prodOptDetails) {
                    prodOptDetailDTO optionDetailDTO = modelMapper.map(optionDetail, prodOptDetailDTO.class);
                    prodOptDetailDTOList.add(optionDetailDTO);
                }
            }
        }

        productDTO.setThumb190(productimg.getThumb190());
        productDTO.setThumb230(productimg.getThumb230());
        productDTO.setThumb456(productimg.getThumb456());
        productDTO.setThumb940(productimg.getThumb940());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("productDTO", productDTO);
        resultMap.put("optionDTOList", optionDTOList);
        resultMap.put("prodOptDetailDTOList", prodOptDetailDTOList);
        return resultMap;
    }
    
    /*
        상품 신규 옵션 등록
        - controller에서 받아온 List<prodOptionDTO> 1,2,3 을 DB에 저장 후 저장된 값 반환
        - DB에 저장할 prodOptDetail에 각 prodOptionDTO의 optNo값이 필요함
        - 저장된 prodOptionDTO의 optNo값을 가지고 모든 옵션의 경우의 수 생성
        - 사용자가 브라우저에서 삭제한 옵션 경우의 수는 제외하고 pordOptDetail 생성 
     */
    public ResponseEntity<?> registerProdOption(List<ProdOptionDTO> optionDTO1, List<ProdOptionDTO> optionDTO2,
                                                List<ProdOptionDTO> optionDTO3, List<prodOptDetailDTO> optDetailDTOS){

        // option1 List를 배열 반복으로 DB에 저장 후 반환 (optNo 필요함)
        List<ProdOption> SaveOptionList1 = new ArrayList<>();
        for (ProdOptionDTO option1 : optionDTO1) {
            ProdOption SaveOption1 = optionRepository.save(modelMapper.map(option1, ProdOption.class));
            SaveOptionList1.add(SaveOption1);
        }
        log.info("SaveOptionList1 : " + SaveOptionList1);

        // option2 List를 배열 반복으로 DB에 저장 후 반환 (optNo 필요함)
        List<ProdOption> SaveOptionList2 = new ArrayList<>();
        if (optionDTO2.get(0).getOptName().equals("0")) {
            ProdOption SaveOption2 = new ProdOption();
            SaveOption2.setOptNo(0);
            SaveOptionList2.add(SaveOption2);
        }else {
            for (ProdOptionDTO option2 : optionDTO2) {
                ProdOption SaveOption2 = optionRepository.save(modelMapper.map(option2, ProdOption.class));
                SaveOptionList2.add(SaveOption2);
            }
        }
        log.info("SaveOptionList2 : " + SaveOptionList2);

        // option3 List를 배열 반복으로 DB에 저장 후 반환 (optNo 필요함)
        List<ProdOption> SaveOptionList3 = new ArrayList<>();
        if (optionDTO3.get(0).getOptName().equals("0")) {
            ProdOption SaveOption3 = new ProdOption();
            SaveOption3.setOptNo(0);
            SaveOptionList3.add(SaveOption3);
        }else {
            for (ProdOptionDTO option3 : optionDTO3) {
                ProdOption SaveOption3 = optionRepository.save(modelMapper.map(option3, ProdOption.class));
                SaveOptionList3.add(SaveOption3);
            }
        }
        log.info("SaveOptionList3 : " + SaveOptionList3);

        /*
            저장된 option1,2,3을 가지고와서 prodOptDetail을 위해 optNo값 매칭
            - for문 3번 반복해서 모든 옵션 경우의 수 저장
            - 브라우저에서 전달 받은 optDetailDTOS와 매칭을 위해 optDetailNo에 임시 번호 부여
         */
        int optDetailNo = 1;
        List<prodOptDetailDTO> optDetailNum = new ArrayList<>();
        for (ProdOption saveOption1 : SaveOptionList1) {
            int saveOpt1No = saveOption1.getOptNo();

            for (ProdOption saveOption2 : SaveOptionList2) {
                int saveOpt2No = saveOption2.getOptNo();

                for (ProdOption saveOption3 : SaveOptionList3) {
                    int saveOpt3No = saveOption3.getOptNo();

                    prodOptDetailDTO optDetailDTO = new prodOptDetailDTO();
                    optDetailDTO.setOptDetailNo(optDetailNo++);
                    optDetailDTO.setOptNo1(saveOpt1No);
                    optDetailDTO.setOptNo2(saveOpt2No);
                    optDetailDTO.setOptNo3(saveOpt3No);
                    optDetailNum.add(optDetailDTO);
                }
            }
        }
        log.info("optDetailNum : " + optDetailNum);

        /*
            optDetailDTOS와 optDetailNum 병합
            - 브라우저에서 받은 optDetailDTOS (각 옵션 경우의 수 별 price, stock값 포함)
            - prodOtion 저장 후 만든 optDetailNum (모든 옵션 경우의 수 별로 optNo1, optNo2, optNo3 매칭)
            - 사용자가 필요 없는 옵션 경우의 수는 삭제해서 서버로 전달하기 때문에 optDetailNo값이 일치 하는 경우만 병합 진행
         */
        List<prodOptDetailDTO> finalOptDetail = new ArrayList<>();
        for (prodOptDetailDTO optDetailDTO : optDetailDTOS) {
            int optDetailNoDTO = optDetailDTO.getOptDetailNo();

            for (prodOptDetailDTO optDetail : optDetailNum) {
                int optDetailNoNum = optDetail.getOptDetailNo();

                // 두 DTO의 optDetailNo가 일치하는 경우 병합 진행
                if (optDetailNoDTO == optDetailNoNum) {
                    prodOptDetailDTO combinedDTO = new prodOptDetailDTO();
                    combinedDTO.setProdNo(optDetailDTO.getProdNo());
                    combinedDTO.setOptPrice(optDetailDTO.getOptPrice());
                    combinedDTO.setOptStock(optDetailDTO.getOptStock());
                    combinedDTO.setOptNo1(optDetail.getOptNo1());
                    combinedDTO.setOptNo2(optDetail.getOptNo2());
                    combinedDTO.setOptNo3(optDetail.getOptNo3());

                    // 합쳐진 리스트에 추가
                    finalOptDetail.add(combinedDTO);
                    break;
                }
            }
        }
        log.info("finalOptDetail : " + finalOptDetail);

        // 완성된 optionDetail DB 저장
        for (prodOptDetailDTO optDetailDTO : finalOptDetail){
            optDetailRepository.save(modelMapper.map(optDetailDTO, ProdOptDetail.class));
        }

        // json 형식으로 변환
        Map<String, List<prodOptDetailDTO>> resultMap = new HashMap<>();
        resultMap.put("finalOptDetail", finalOptDetail);
        return ResponseEntity.ok().body(resultMap);
    }

    // 상품 카테고리 수정
    public ResponseEntity<?> modifyCate(Map<String, Object> requestData) {
        int prodNo = Integer.parseInt((String)requestData.get("prodNo"));
        String cate01No = requestData.get("cate01No").toString();
        String cate02No = requestData.get("cate02No").toString();
        String cate03No = requestData.get("cate03No").toString();

        Optional<Product> optionalProduct = productRepository.findById(prodNo);

        Map<String, Integer> resultMap = new HashMap<>();
        if (optionalProduct.isPresent()) {
            String saveCode = cate01No + cate02No + cate03No;
            optionalProduct.get().setCateCode(saveCode);
            Product saveCate = productRepository.save(optionalProduct.get());
            if (saveCate.getCateCode().equals(saveCode)) {
                resultMap.put("result", 1);
                return ResponseEntity.status(HttpStatus.OK).body(resultMap);
            }else {
                resultMap.put("result", 0);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
            }
        }else {
            resultMap.put("result", 0);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        }
    }

    // 판매자 상품 기본 정보 변경
    public int modifyProdInfo(ProductDTO productDTO, ProductimgDTO productimgDTO) {

        Optional<Product> optProduct = productRepository.findById(productDTO.getProdNo());

        if (optProduct.isPresent()) {
            optProduct.get().setProdName(productDTO.getProdName());
            optProduct.get().setProdInfo(productDTO.getProdInfo());
            optProduct.get().setProdCompany(productDTO.getProdCompany());
            optProduct.get().setProdPrice(productDTO.getProdPrice());
            optProduct.get().setProdDiscount(productDTO.getProdDiscount());
            optProduct.get().setProdPoint(productDTO.getProdPoint());
            optProduct.get().setProdStock(productDTO.getProdStock());
            optProduct.get().setProdDeliveryFee(productDTO.getProdDeliveryFee());

            productRepository.save(optProduct.get());
        }else {
            return 0;
        }

        Productimg optProductImg = productimgRepository.findByProdNo(productDTO.getProdNo());

        if (optProductImg != null) {
            if (!productimgDTO.getMultThumb190().isEmpty()) {
                //int result = deleteFile(optProductImg.getThumb190());
                int result = 1;
                if (result > 0) {
                    optProductImg.setThumb190(fileUpload(productimgDTO.getMultThumb190(), "thumb190"));
                }
            }
            if (!productimgDTO.getMultThumb230().isEmpty()) {
                //int result = deleteFile(optProductImg.getThumb230());
                int result = 1;
                if (result > 0) {
                    optProductImg.setThumb230(fileUpload(productimgDTO.getMultThumb230(), "thumb230"));
                }
            }
            if (!productimgDTO.getMultThumb456().isEmpty()) {
                //int result = deleteFile(optProductImg.getThumb456());
                int result = 1;
                if (result > 0) {
                    optProductImg.setThumb456(fileUpload(productimgDTO.getMultThumb456(), "thumb456"));
                }
            }
            if (!productimgDTO.getMultThumb940().isEmpty()) {
                //int result = deleteFile(optProductImg.getThumb940());
                int result = 1;
                if (result > 0) {
                    optProductImg.setThumb940(fileUpload(productimgDTO.getMultThumb940(), "thumb940"));
                }
            }
            productimgRepository.save(optProductImg);
            return 1;
        }else {
            return 0;
        }
    }

    // 첨부 파일 삭제
    public int deleteFile(String sName) {
        File deleteFile = new File(fileUploadPath, sName);
        if (deleteFile.delete()){
            log.info("파일 삭제 service11 파일 삭제 성공");
            return 1;
        }else{
            log.info("파일 삭제 service11 파일 삭제 실패");
            return 0;
        }
    }

}
