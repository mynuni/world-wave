package com.my.worldwave.security;

import com.my.worldwave.exception.auth.AccessTokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof AccessTokenExpiredException) {
            // Access Token이 만료된 경우 401을 반환한다.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            // 인증 필터를 지난 후 AccessDenied에 넘겨지지 않은 예외가 발생한 경우 403을 반환한다.
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

}
