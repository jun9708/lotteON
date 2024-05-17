package kr.co.lotteon.intercepter;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lotteon.config.AppInfo;
import kr.co.lotteon.dto.Cate01DTO;
import kr.co.lotteon.dto.Cate02DTO;
import kr.co.lotteon.dto.Cate03DTO;
import kr.co.lotteon.service.ProdCateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public class Appinfointercepter implements HandlerInterceptor {


    private final AppInfo appInfo;
    private final ProdCateService prodCateService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            // 카테고리 정보 조회 및 모델에 추가
            Map<String, List<?>> cateMap = prodCateService.selectProdCate();
            List<Cate01DTO> cate01DTOs = (List<Cate01DTO>) cateMap.get("cate01DTOs");
            List<Cate02DTO> cate02DTOs = (List<Cate02DTO>) cateMap.get("cate02DTOs");
            List<Cate03DTO> cate03DTOs = (List<Cate03DTO>) cateMap.get("cate03DTOs");

            modelAndView.addObject("cate01DTOs", cate01DTOs);
            modelAndView.addObject("cate02DTOs", cate02DTOs);
            modelAndView.addObject("cate03DTOs", cate03DTOs);

            // 모든 요청에 대해 appInfo 모델 참조
            modelAndView.addObject(appInfo);
        }
    }
}
