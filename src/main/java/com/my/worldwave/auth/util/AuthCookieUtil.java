package com.my.worldwave.auth.util;

import com.my.worldwave.util.CookieUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.my.worldwave.auth.util.AuthenticationConstants.SIGN_UP_PENDING_EMAIL_EXP;

@Component
public class AuthCookieUtil {

    private final long REFRESH_TOKEN_EXP;
    private final CookieUtil cookieUtil;

    public AuthCookieUtil(@Value("${jwt.refresh-token-exp}") long REFRESH_TOKEN_EXP, CookieUtil cookieUtil) {
        this.REFRESH_TOKEN_EXP = REFRESH_TOKEN_EXP;
        this.cookieUtil = cookieUtil;
    }

    public String createRefreshTokenCookie(String refreshToken) {
        return cookieUtil.createSecureCookie(TokenType.REFRESH_TOKEN.getTokenName(), refreshToken, REFRESH_TOKEN_EXP);
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = cookieUtil.getCookie(request, TokenType.REFRESH_TOKEN.getTokenName());
        return cookie.getValue();
    }

    public void deleteRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        cookieUtil.deleteCookie(request, response, TokenType.REFRESH_TOKEN.getTokenName());
    }

}
