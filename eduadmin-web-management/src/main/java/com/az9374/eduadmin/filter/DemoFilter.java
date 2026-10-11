package com.az9374.eduadmin.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
//@WebFilter("/*")
public class DemoFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("拦截到了请求...");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("destroy....");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init....");
    }
}
