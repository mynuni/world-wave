package com.my.worldwave.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailureException extends AuthenticationException {

    private static final String MESSAGE = "인증 정보가 올바르지 않습니다.";

    public AuthenticationFailureException() {
        super(MESSAGE);
    }

}
