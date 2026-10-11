package com.az9374.eduadmin.interceptor;

import com.az9374.eduadmin.utils.CurrentHolder;
import com.az9374.eduadmin.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
/*
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求路径
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse resp = (HttpServletResponse) response;
//        String path = request.getServletPath();
//        //2.判断请求路径是否为登录路径,是，登录路径放行
//        if(path.contains("/login") ){
//            log.info("登录操作");
//
//            return true;//下面的不用继续了
//        }
        //3.否，获取请求头token
        String token = request.getHeader("token");
        //4.判断token是否为空，为空，未登录，返回错误信息401状态码
        if (token==null){
            log.info("token为Null...");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //5.不为空，解析token，解析失败抛异常->401状态码
        try {
            Claims claims= JwtUtils.parseJwt(token);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentId(empId);
            log.info("操作Id为 : {}", empId);
        }
        catch (Exception e){
            log.info("解析token失败...");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //6.解析成功，放行
        log.info("token解析成功...");
        return true;
    }*/
}
