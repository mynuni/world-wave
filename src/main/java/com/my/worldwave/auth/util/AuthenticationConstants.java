package com.my.worldwave.auth.util;

// 인증 관련 상수 모음
public abstract class AuthenticationConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final long SIGN_UP_PENDING_EMAIL_EXP = 10 * 60 * 1_000L;

}
