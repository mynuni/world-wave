package com.my.worldwave.security;

import com.my.worldwave.auth.util.AuthCookieUtil;
import com.my.worldwave.member.entity.Role;
import com.my.worldwave.util.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final String GOOGLE_SUCCESS_URI;
    private final String OAUTH2_SIGN_UP_URI;
    private final JwtTokenService jwtTokenService;
    private final AuthCookieUtil authCookieUtil;
    private final RedisService redisService;

    public OAuth2AuthenticationSuccessHandler(@Value("${oauth2.google.success-redirect-uri}") String googleSuccessUri,
                                              @Value("${oauth2.sign-up-uri}") String oauth2SignUpUri,
                                              JwtTokenService jwtTokenService, AuthCookieUtil authCookieUtil, RedisService redisService) {
        this.GOOGLE_SUCCESS_URI = googleSuccessUri;
        this.OAUTH2_SIGN_UP_URI = oauth2SignUpUri;
        this.jwtTokenService = jwtTokenService;
        this.authCookieUtil = authCookieUtil;
        this.redisService = redisService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        // 신규 유저인 경우 이메일을 토큰화하여 회원 가입 페이지로 리다이렉트
        if (userDetails.getMember().getRole() == Role.GUEST) {
            String emailToken = jwtTokenService.generateEmailToken(email);
            String redirectUrl = createRedirectUrl("token", emailToken, OAUTH2_SIGN_UP_URI);
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            return;
        }

        // 기존 유저인 경우 JWT를 발급하고 리다이렉트
        String accessToken = jwtTokenService.generateAccessToken(email, authentication.getAuthorities());
        String refreshToken = jwtTokenService.generateRefreshToken(email);
        redisService.saveRefreshToken(refreshToken, email);
        String refreshTokenCookie = authCookieUtil.createRefreshTokenCookie(refreshToken);
        response.setHeader("Set-Cookie", refreshTokenCookie);
        getRedirectStrategy().sendRedirect(request, response, createRedirectUrl("accessToken", accessToken, GOOGLE_SUCCESS_URI));
    }

    private String createRedirectUrl(String paramName, String value, String location) {
        return UriComponentsBuilder.fromUriString(location)
                .queryParam(paramName, value)
                .build()
                .toUriString();
    }

}
