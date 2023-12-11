package com.my.worldwave.exception.auth;

public class EmailVerificationException extends RuntimeException {

    private static final String message = "인증 번호가 올바르지 않거나 만료된 코드입니다.";

    public EmailVerificationException() {
        super(message);
    }
}
