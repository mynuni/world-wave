package com.my.worldwave.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.my.worldwave.security.JwtTokenService.AUTHORIZATION_HEADER;
import static com.my.worldwave.security.JwtTokenService.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);

        if (token != null && jwtTokenService.isValidToken(token)) {
            Authentication authentication = jwtAuthenticationProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String headerValue = request.getHeader(AUTHORIZATION_HEADER);

        if (headerValue != null && headerValue.startsWith(BEARER_PREFIX)) {
            return headerValue.substring(BEARER_PREFIX.length());
        }

        return null;

    }

}
