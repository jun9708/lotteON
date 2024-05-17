package kr.co.lotteon.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.*;
import kr.co.lotteon.entity.*;
import kr.co.lotteon.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final CartRepository cartRepository;
    private final UserCouponRepository userCouponRepository;


    // email 전송
    private final JavaMailSender javaMailSender;
    private final HttpSession httpSession;

    // 회원가입 유저 정보 중복 체크
    public int registerUserCheck(HttpSession session, String type, String value){
        int result = 0; // 0은 사용 가능, 1은 사용 불가능

        // User 테이블 조회를 통해 중복 여부 확인
        if (type.equals("userId")) {
            // 아이디 중복 검사
            Optional<User> optUser = userRepository.findById(value);
            // optional이 비어있는지 체크
            if (optUser.isPresent()) {
                // 사용 불가능
                result = 1;
                return result;
            } else {
                // 사용 가능
                return result;
            }
        }else if(type.equals("userHp")) {
            //전화번호 중복검사
            Optional<User> optUser = userRepository.findByUserHp(value);
            //Optional이 비어있는지 체크
            if(optUser.isPresent()){
                // 사용 불가능
                result = 1;
                return result;
            }else {
                // 사용가능
                return result;
            }
        }  else if (type.equals("company")){
            //회사명 중복검사
            Optional<Seller> optSeller = sellerRepository.findByCompany(value);
            //Optional 비어있는지 체크
            if(optSeller.isPresent()){
                //사용 불가능
                result = 1;
                return  result;
            }else {
                // 사용 가능
                return result;
            }
        } else if (type.equals("licenseNum")) {
            //통신판매업 중복검사
            Optional<Seller> optSeller = sellerRepository.findByLicenseNum(value);
            //Optional 비어있는지 체크
            if (optSeller.isPresent()) {
                //사용 불가능
                result = 1;
                return result;
            } else {
                // 사용 가능
                return result;
            }
        } else if (type.equals("businessNum")) {
            Optional<Seller> optSeller = sellerRepository.findByBusinessNum(value);
            //Optional 비어있는지 체크
            if (optSeller.isPresent()) {
                //사용 불가능
                result = 1;
                return result;
            }else {
                //사용 가능
                return  result;
            }
        } else if (type.equals("sellerName")){
            //판매자이름 중복검사
            Optional<Seller> optSeller = sellerRepository.findBySellerName(value);
            //Optional 비어있는지 체크
            if (optSeller.isPresent()) {
                //사용 불가능
                result = 1;
                return result;
            }else {
                //사용 가능
                return  result;
            }
        } else if (type.equals("userEmail")) {
            //이메일 중복검사
            Optional<User> optUser = userRepository.findByUserEmail(value);
            //Optional이 비어있는지 체크
            if(optUser.isPresent()){
                //사용 불가능
                result = 1;
                return  result;
            }else {
                // 사용 가능
                // 인증코드 발송
                sendEmailConde(session ,value);
                return result;
            }
        } else if (type.equals("sellerHp")){
            //판매자번호 중복검사
            Optional<Seller> optSeller = sellerRepository.findBySellerHp(value);
            //Optional 비어있는지 체크
            if (optSeller.isPresent()) {
                //사용 불가능
                result = 1;
                return result;
            }else {
                //사용 가능
                return  result;
            }
        }else if(type.equals("fax")){
            Optional<Seller> optSeller = sellerRepository.findByFax(value);
            //Optional 비어있는지 체크
            if (optSeller.isPresent()) {
                //사용 불가능
                result = 1;
                return result;
            }else {
                //사용 가능
                return  result;
            }
        }
        return result;
    }

    @Value("${spring.mail.username}")
    private String sender;
    // 🎈이메일 인증코드 전송
    public void sendEmailConde(HttpSession session, String receiver){
        log.info("sender : " + sender);

        // MimeMessage 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        // 인증코드 생성 후 세션 저장
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        session.setAttribute("code", String.valueOf(code));

        log.info("code : " + code);

        String title = "🌻lotteon 인증코드 입니다.";
        String content = "<h1>인증코드는" +  code + "입니다.</h1>";

        try {
            message.setFrom(new InternetAddress(sender, "보내는 사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");

            javaMailSender.send(message);

        } catch(Exception e){
            log.error("sendEmailCode : " + e.getMessage());
        }
    }
    // UserId 찾기
    public Optional<User> findUserIdByUserNameAndUserEmail(String userName, String userEmail, HttpSession session) {
        return userRepository.findUserIdByUserNameAndUserEmail(userName, userEmail);
    }
    // 아이디찾기 이메일 확인/발송
    public int findIdCheckEmail(HttpSession session, String value) {
        int result = 0;

        //이메일 중복검사
        Optional<User> optUser = userRepository.findByUserEmail(value);
        //Optional이 비어있는지 체크
        if (optUser.isPresent()) {
            //사용 가능
            // 인증코드 발송
            sendEmailConde(session, value);
            return result;
        } else {
            // 사용 불가능
            result = 1;
            return result;
        }
    }
    // UserPw 수정
    public long updatePw(String userId,String userPw, String userEmail, HttpSession session) {
        String encodedPassword = passwordEncoder.encode(userPw);
        return userRepository.updateUserPwByUserIdAndUserEmail(userId, encodedPassword, userEmail);
    }
    // 비밀번호재설정 이메일 확인/발송
    public int updatePwCheckEmail(HttpSession session, String email, String userId) {
        int result = 0;
        log.info("email, userId" + email + userId);
        //이메일 중복검사
        Optional<User> optUser = userRepository.findByUserEmailAndUserId(email, userId);
        //Optional이 비어있는지 체크
        if (optUser.isPresent()) {
            //사용 가능
            // 인증코드 발송
            sendEmailConde(session, email);
            result = 1;
            log.info("성공 : " + result);
            return result;
        } else {
            // 사용 불가능
            log.info("실패 : " + result);
            return result;
        }
    }


    //User 회원 등록
    public User registerUser(UserDTO userDTO){
        String encoded = passwordEncoder.encode(userDTO.getUserPw());
        userDTO.setUserPw(encoded);

        User user = modelMapper.map(userDTO, User.class);
        user.setUserVisitDate(user.getUserRegDate());
        user.setUserUpdate(user.getUserRegDate());
        User savedUser = userRepository.save(user);


        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(savedUser.getUserId());
        userPoint.setPointBalance(0);
        userPointRepository.save(userPoint);

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(savedUser.getUserId());
        userCoupon.setCpNo(1);
        userCoupon.setUcpStatus("사용 가능");
        userCouponRepository.save(userCoupon);

        Cart cart = new Cart();
        cart.setUserId(savedUser.getUserId());
        cartRepository.save(cart);
        return savedUser;
    }
    // UserId 찾기(if optional = null -> return Optional.empty();)
    public Optional<User> findUserIdByUserNameAndUserEmail(String userName, String userEmail) {
        return userRepository.findUserIdByUserNameAndUserEmail(userName, userEmail);
    }

    //Seller 회원 등록
    public int registerSeller(UserDTO userDTO){
        // 랜덤 난수로 sellerId 생성
        String sellerNo = UUID.randomUUID().toString().substring(0, 8) + userDTO.getUserId();
        String encoded = passwordEncoder.encode(userDTO.getUserPw());

        SellerDTO saveSellerDTO = new SellerDTO();
        userDTO.setUserPw(encoded);
        saveSellerDTO.setSellerNo(sellerNo);
        saveSellerDTO.setUserId(userDTO.getUserId());
        saveSellerDTO.setSellerName(userDTO.getSellerName());
        saveSellerDTO.setSellerHp(userDTO.getSellerHp());
        saveSellerDTO.setCompany(userDTO.getCompany());
        saveSellerDTO.setBusinessNum(userDTO.getBusinessNum());
        saveSellerDTO.setLicenseNum(userDTO.getLicenseNum());
        saveSellerDTO.setSellerGrade(userDTO.getSellerGrade());
        saveSellerDTO.setFax(userDTO.getFax());

        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);

        Seller seller = modelMapper.map(saveSellerDTO, Seller.class);
        Seller savedSeller = sellerRepository.save(seller);

        int result = 0;
        if (savedUser.getUserId().equals(userDTO.getUserId())  && savedSeller.getUserId().equals(userDTO.getUserId())) {
            result = 1;
        }
        return result;
    }
    // 관리자 - 회원관리 - 회원현황 //
    public PageResponseDTO selectMemberList(PageRequestDTO pageRequestDTO){
        Pageable pageable = pageRequestDTO.getPageable("No");

        Page<User> pageUser =  userRepository.selectSellerList(pageable, pageRequestDTO);
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User eachUser : pageUser.getContent()) {
            userDTOs.add(modelMapper.map(eachUser, UserDTO.class));
        }

        int total = (int) pageUser.getTotalElements();
        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(userDTOs)
                .total(total)
                .build();
    }
    // 관리자 - 회원관리 - 회원 정보 변경 //
    public ResponseEntity<?> changeUserInfo(String userId, String changeType, String changeValue){

        Optional<User> optUser = userRepository.findById(userId);
        User resultUser = null;
        if (optUser.isPresent()){
            if (changeType.equals("userRole")) {
                if (changeValue.equals("DELETE")){
                    optUser.get().setUserStatus("탈퇴회원");
                }
                optUser.get().setUserRole(changeValue);
            }else if (changeType.equals("userGrade")) {
                optUser.get().setUserGrade(changeValue);
            }else if (changeType.equals("userStatus")) {
                if (changeValue.equals("탈퇴회원")){
                    optUser.get().setUserRole("DELETE");
                }
                optUser.get().setUserStatus(changeValue);
            }
            optUser.get().setUserUpdate(LocalDateTime.now());
            resultUser = userRepository.save(optUser.get());
        }

        Map<String, Integer> resultMap = new HashMap<>();
        if (resultUser.getUserRole().equals(changeValue)
                || resultUser.getUserGrade().equals(changeValue)
                || resultUser.getUserStatus().equals(changeValue)) {
            resultMap.put("result", 1);
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        }else {
            resultMap.put("result", 0);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        }
    }
    // 관리자 - 회원관리 - 포인트관리 //
    public PageResponseDTO userPointControl(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("No");
        return userPointRepository.userPointControl(pageRequestDTO, pageable);
    }

    // 관리자 - 회원관리 - 포인트관리 - 포인트 지급 & 회수 //
    public ResponseEntity<?> pointControl(String type, String userId, String changePoint, String changeCode) {
        UserPoint userPoint = userPointRepository.findByUserId(userId);

        PointHistory pointHistory = new PointHistory();
        pointHistory.setPointNo(userPoint.getPointNo());
        pointHistory.setChangeCode(changeCode);
        if (type.equals("plus")) {
            pointHistory.setChangeType("적립");
            pointHistory.setChangePoint(Integer.parseInt(changePoint));
            userPoint.setPointBalance(userPoint.getPointBalance() + Integer.parseInt(changePoint));
        }else {
            pointHistory.setChangeType("사용");
            pointHistory.setChangePoint(Integer.parseInt("-" + changePoint));
            userPoint.setPointBalance(userPoint.getPointBalance() - Integer.parseInt(changePoint));
        }
        UserPoint updatePoint = userPointRepository.save(userPoint);
        PointHistory updateHistory = pointHistoryRepository.save(pointHistory);

        Map<String, UserPoint> resultMap = new HashMap<>();
        if (updateHistory.getChangeCode().equals(changeCode)) {
            resultMap.put("result", updatePoint);
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        }else {
            resultMap.put("result", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        }
    }
}
