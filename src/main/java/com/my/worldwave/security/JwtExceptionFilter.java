package com.my.worldwave.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.my.worldwave.exception.ExceptionConstants.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (MalformedJwtException e) {
            setResponse(response, HttpServletResponse.SC_BAD_REQUEST, MALFORMED_JWT);
        } catch (SignatureException e) {
            setResponse(response, HttpServletResponse.SC_BAD_REQUEST, SIGNATURE_ERROR);
        } catch (ExpiredJwtException e) {
            setResponse(response, HttpServletResponse.SC_UNAUTHORIZED, EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            setResponse(response, HttpServletResponse.SC_BAD_REQUEST, UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            setResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
        }
    }

    private void setResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

}
