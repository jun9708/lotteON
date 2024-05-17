package kr.co.lotteon.service.seller;

import com.querydsl.core.Tuple;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderdetailRepository;
    private final ProdQnaRepository prodQnaRepository;
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    // 판매자 관리페이지 - 홈
    public SellerInfoDTO selectSellerInfo(HttpSession session, String UserId){

        Optional<Seller> optSeller = sellerRepository.findByUserId(UserId);
        String prodSeller = null;
        if (optSeller.isPresent()){
            prodSeller= optSeller.get().getSellerNo();
            session.setAttribute("prodSeller", optSeller.get().getSellerNo());
            session.setAttribute("sellerCompany", optSeller.get().getCompany());
        }
        return sellerRepository.selectSellerInfo(prodSeller);
    }

    // 관리자 메인페이지 - 홈
    public SellerInfoDTO selectAdminInfo(){
        return sellerRepository.selectAdminInfo();
    }

    // 판매자 관리페이지 - 상품목록 - 상품관리 (전체 상품 목록)
    public ProductPageResponseDTO selectProductForSeller(String prodSeller, ProductPageRequestDTO productPageRequestDTO){

        Pageable pageable = productPageRequestDTO.getPageable("prodNo");

        Page<Tuple> pageProducts = sellerRepository.selectProductForSeller(prodSeller, productPageRequestDTO, pageable);

        List<ProductDTO> dtoList = pageProducts.getContent().stream()
                .map(tuple -> {
                    Product product = tuple.get(0, Product.class);
                    String thumb190 = tuple.get(1, String.class);
                    product.setThumb190(thumb190);
                    return modelMapper.map(product, ProductDTO.class);
                })
                .toList();
        int total = (int) pageProducts.getTotalElements();

        return ProductPageResponseDTO.builder()
                .productPageRequestDTO(productPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 판매자 관리페이지 - 상품목록 - 상품관리 (검색 상품 목록)
    public ProductPageResponseDTO searchProductForSeller(String prodSeller, ProductPageRequestDTO productPageRequestDTO){

        Pageable pageable = productPageRequestDTO.getPageable("prodNo");

        Page<Tuple> pageProducts = sellerRepository.searchProductForSeller(prodSeller, productPageRequestDTO, pageable);

        List<ProductDTO> dtoList = pageProducts.getContent().stream()
                .map(tuple -> {
                    Product product = tuple.get(0, Product.class);
                    String thumb190 = tuple.get(1, String.class);
                    product.setThumb190(thumb190);
                    return modelMapper.map(product, ProductDTO.class);
                })
                .toList();
        int total = (int) pageProducts.getTotalElements();

        return ProductPageResponseDTO.builder()
                .productPageRequestDTO(productPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 최근 한달치 주문 건수 //
    public LinkedHashMap<String, Integer> selectProdSalesCount(String prodSeller) {
        // 최근 한달치 주문 데이터 조회
        List<OrderDetail> selectOrders = sellerRepository.selectSalesForMonth(prodSeller);

        // 모든 날짜를 포함하는 기간을 정의
        LocalDate oneMonthsAgo = LocalDate.now().minusMonths(1);
        List<LocalDate> allDatesInRange = Stream.iterate(oneMonthsAgo, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(oneMonthsAgo, LocalDate.now()) + 1)
                .collect(Collectors.toList());

        LinkedHashMap<String, Integer> orderByDate = new LinkedHashMap<>();

        for (int i = allDatesInRange.size() - 1; i >= 0; i--) {
            String localDate = allDatesInRange.get(i).toString().substring(5, 10);
            orderByDate.put(localDate, 0);
        }

        for (OrderDetail order : selectOrders) {
            String date = order.getDetailDate().toString().substring(5, 10);
            if (orderByDate.containsKey(date)) {
                orderByDate.put(date, orderByDate.get(date)+1);
            }else {
                orderByDate.put(date, 1);
            }
        }
        log.info("orderByDate : " + orderByDate);
        return orderByDate;
    }

    // 관리자 메인페이지 - 고객문의 최신순 5개 조회
    // 판매자 메인페이지 - 고객문의 최신순 5개 조회
    public List<ProdQnaDTO> selectProdQnaForIndex(String prodSeller) {
        List<ProdQna> prodQna = prodQnaRepository.selectProdQnaForIndex(prodSeller);
        List<ProdQnaDTO> prodQnaDTO = new ArrayList<>();
        for (ProdQna eachQna : prodQna) {
            ProdQnaDTO eachQnaDTO = modelMapper.map(eachQna, ProdQnaDTO.class);
            prodQnaDTO.add(eachQnaDTO);
        }
        return prodQnaDTO;
    }

    // 관리자 메인페이지 - 공지사항 최신순 5개 조회
    // 판매자 메인페이지 - 공지사항 최신순 5개 조회
    public List<NoticeDTO> selectNoticeForIndex() {
        List<Notice> notice = noticeRepository.findTop5ByOrderByNoticeDateDesc();
        List<NoticeDTO> noticeDTO = new ArrayList<>();
        for (Notice eachNotice : notice) {
            NoticeDTO eachNoticeDTO = modelMapper.map(eachNotice, NoticeDTO.class);
            noticeDTO.add(eachNoticeDTO);
        }
        return noticeDTO;
    }

    // 판매자의 판매 목록 최신순 //
    public PageResponseDTO selectProdSalesInfo(String prodSeller, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("No");

        Page<Tuple> orderResults = sellerRepository.selectProdSalesInfo(prodSeller, pageRequestDTO, pageable);

        List<OrderDetailDTO> dtoList = orderResults.stream()
                .map(tuple -> {
                    OrderDetail orderDetail = tuple.get(0, OrderDetail.class);
                    String prodName = tuple.get(1, String.class);
                    Orders orders = tuple.get(2, Orders.class);
                    OrderDetailDTO orderDetailDTO = modelMapper.map(orderDetail, OrderDetailDTO.class);
                    orderDetailDTO.setProdName(prodName);
                    orderDetailDTO.setUserId(orders.getUserId());
                    orderDetailDTO.setOrderReceiver(orders.getOrderReceiver());
                    orderDetailDTO.setOrderHp(orders.getOrderHp());
                    orderDetailDTO.setOrderPay(orders.getOrderPay());
                    orderDetailDTO.setOrderMemo(orders.getOrderMemo());
                    orderDetailDTO.setOrderAddr(orders.getOrderAddr());
                    return orderDetailDTO;
                })
                .toList();

        int total = (int) orderResults.getTotalElements();
        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 최근 한달 일자별 주문 금액 합산
    public LinkedHashMap<String, Integer> selectSalesForMonth(String prodSeller){
        // 최근 한달치 주문 데이터 조회
        List<OrderDetail> selectOrders = sellerRepository.selectSalesForMonth(prodSeller);

        // 모든 날짜를 포함하는 기간을 정의
        LocalDate oneMonthsAgo = LocalDate.now().minusMonths(1);
        List<LocalDate> allDatesInRange = Stream.iterate(oneMonthsAgo, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(oneMonthsAgo, LocalDate.now()) + 1)
                .collect(Collectors.toList());

        LinkedHashMap<String, Integer> orderByDate = new LinkedHashMap<>();

        for (int i = allDatesInRange.size() - 1; i >= 0; i--) {
            String localDate = allDatesInRange.get(i).toString().substring(5, 10);
            orderByDate.put(localDate, 0);
        }

        for (OrderDetail order : selectOrders) {
            String date = order.getDetailDate().toString().substring(5, 10);
            if (orderByDate.containsKey(date)) {
                int eachPrice = order.getDetailPrice();

                orderByDate.put(date, orderByDate.get(date)+eachPrice);
            }else {
                orderByDate.put(date, order.getDetailPrice());
            }
        }
        return orderByDate;
    }

    // 판매자의 기간별 매출, 취소, 환불 금액 합산
    public Map<String, Map<String, Integer>> selectSalesAverages(String prodSeller) {
        // 전체 기간의 매출, 취소, 환불 금액
        List<OrderDetail> ordersForTotal = sellerRepository.selectSalesForTotal(prodSeller);
        // 일년의 매출, 취소, 환불 금액
        List<OrderDetail> ordersForYear = sellerRepository.selectSalesForYear(prodSeller);
        // 한달의 매출, 취소, 환불 금액
        List<OrderDetail> ordersForMonth = sellerRepository.selectSalesForMonth(prodSeller);
        // 일주일의 매출, 취소, 환불 금액
        List<OrderDetail> ordersForWeek = sellerRepository.selectSalesForWeek(prodSeller);

        // 각 기간별 상태별 금액  합산
        Map<String, Integer> totalAverages = calculatePriceByPeriod(ordersForTotal);
        Map<String, Integer> yearAverages = calculatePriceByPeriod(ordersForYear);
        Map<String, Integer> monthAverages = calculatePriceByPeriod(ordersForMonth);
        Map<String, Integer> weekAverages = calculatePriceByPeriod(ordersForWeek);

        // 각 Map들을 하나로 묶어서 리턴
        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
        resultMap.put("total", totalAverages);
        resultMap.put("year", yearAverages);
        resultMap.put("month", monthAverages);
        resultMap.put("week", weekAverages);
        return resultMap;
    }
    
    // 매출, 취소, 환불 상태별로 금액 합산하는 메서드
    public Map<String, Integer> calculatePriceByPeriod(List<OrderDetail> ordersByPeriod) {
        // 상태별 금액 합산을 저장할 Map 생성 후 초기값 세팅
        Map<String, Integer> orderByCondition = new HashMap<>();
        orderByCondition.put("sales", 0);
        orderByCondition.put("cancel", 0);
        orderByCondition.put("refund", 0);
        // for문으로 상태별로 금액 합산
        for (OrderDetail order : ordersByPeriod) {
            if (order.getDetailStatus().contains("배송")) {
                int eachPrice = order.getDetailPrice();
                orderByCondition.put("sales", orderByCondition.get("sales")+eachPrice);
            }else if (order.getDetailStatus().contains("취소")) {
                int eachPrice = order.getDetailPrice();
                orderByCondition.put("cancel", orderByCondition.get("cancel")+eachPrice);
            }else if (order.getDetailStatus().contains("환불")) {
                int eachPrice = order.getDetailPrice();
                orderByCondition.put("refund", orderByCondition.get("refund")+eachPrice);
            }
        }
        return orderByCondition;
    }

    // 최근 일주일 일자별 주문 상세 (매출, 취소, 교환, 환불 금액 / 건수)
    public LinkedHashMap<String, OrderPriceCountDTO> selectSalesForWeek(String prodSeller) {
        // 일주일의 매출, 취소, 환불 금액
        List<OrderDetail> ordersForWeek = sellerRepository.selectSalesForWeek(prodSeller);

        // 일주일 기간을 정의
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        List<LocalDate> allDatesInRange = Stream.iterate(oneWeekAgo, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(oneWeekAgo, LocalDate.now()) + 1)
                .collect(Collectors.toList());

        LinkedHashMap<String, OrderPriceCountDTO> orderByDate = new LinkedHashMap<>();
        for (int i = allDatesInRange.size() - 1; i >= 0; i--) {
            String localDate = allDatesInRange.get(i).toString().substring(5, 10);
            orderByDate.put(localDate, new OrderPriceCountDTO());
        }

        for (OrderDetail order : ordersForWeek) {
            String date = order.getDetailDate().toString().substring(5, 10);
            if (orderByDate.containsKey(date)) {
                int eachPrice = order.getDetailPrice();
                OrderPriceCountDTO value = orderByDate.get(date);

                if (order.getDetailStatus().contains("취소")) {
                    value.setCancelCount(value.getCancelCount()+1);
                    value.setCancelPrice(value.getCancelPrice()+eachPrice);
                }else if (order.getDetailStatus().contains("교환")) {
                    value.setExchangeCount(value.getExchangeCount()+1);
                    value.setExchangePrice(value.getExchangePrice()+eachPrice);
                }else if (order.getDetailStatus().contains("환불")) {
                    value.setRefundCount(value.getRefundCount()+1);
                    value.setRefundPrice(value.getRefundPrice()+eachPrice);
                }else {
                    value.setSalesCount(value.getSalesCount()+1);
                    value.setSalesPrice(value.getSalesPrice()+eachPrice);
                }
                orderByDate.put(date, value);
            }
        }
        log.info("orderByDate : " + orderByDate.toString());
        return orderByDate;
    }

    // 판매 상품 주문 상태 변경
    public int updateStatus(int detailNo, String detailStatus) {
        Optional<OrderDetail> optDetail = orderdetailRepository.findById(detailNo);
        if (optDetail.isPresent()) {
            optDetail.get().setDetailStatus(detailStatus);
            OrderDetail result = orderdetailRepository.save(optDetail.get());
            if (result.getDetailStatus().equals(detailStatus)) {
                return 1;
            }else {
                return 0;
            }
        }else {
            return 0;
        }
    }

    // 관리자 - 상점관리 - 판매자현황 //
    public PageResponseDTO selectSellerList(PageRequestDTO pageRequestDTO){
        Pageable pageable = pageRequestDTO.getPageable("No");

        return sellerRepository.selectSellerList(pageable, pageRequestDTO);
    }

    // 판매 상품 판매 상태 변경 //
    public ResponseEntity<?> modifyProdStatus(int prodNo, int status) {
        Optional<Product> optProduct = productRepository.findById(prodNo);
        Map<String, Integer> resultMap = new HashMap<>();

        if (status == 1) {
            if (optProduct.isPresent()) {
                optProduct.get().setProdStatus("판매중단");
                Product saveProduct = productRepository.save(optProduct.get());
                if (saveProduct.getProdStatus().equals("판매중단")) {
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
        }else {
            if (optProduct.isPresent()) {
                optProduct.get().setProdStatus("새상품");
                Product saveProduct = productRepository.save(optProduct.get());
                if (saveProduct.getProdStatus().equals("새상품")) {
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
    }

}
