package com.az9374.eduadmin.filter;

import com.az9374.eduadmin.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //1.获取请求路径
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getServletPath();
        //2.判断请求路径是否为登录路径,是，登录路径放行
        if(path.contains("/login") ){
            log.info("登录操作");
            chain.doFilter(request,response);
            return;//下面的不用继续了
        }
        //3.否，获取请求头token
        String token = req.getHeader("token");
        //4.判断token是否为空，为空，未登录，返回错误信息401状态码
        if (token==null){
            log.info("token为Null...");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //5.不为空，解析token，解析失败抛异常->401状态码
        try {
            JwtUtils.parseJwt(token);
        }
        catch (Exception e){
            log.info("解析token失败...");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //6.解析成功，放行
        log.info("token解析成功...");
        chain.doFilter(request, response);
        return;
    }
}
