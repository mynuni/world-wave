package com.my.worldwave.auth.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if ("GET".equals(request.getMethod())) {
            return true;
        }

        if (session == null || session.getAttribute("authUser") == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}
