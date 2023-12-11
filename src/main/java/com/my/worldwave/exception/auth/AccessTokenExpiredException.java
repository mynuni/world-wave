package com.my.worldwave.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class AccessTokenExpiredException extends AuthenticationException {

    private static final String MESSAGE = "만료된 토큰입니다.";

    public AccessTokenExpiredException() {
        super(MESSAGE);
    }

}
