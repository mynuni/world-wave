package com.my.worldwave.exception.member;

public class AuthenticationFailureException extends RuntimeException {

    private static final String MESSAGE = "아이디 또는 비밀번호가 올바르지 않습니다.";

    public AuthenticationFailureException() {
        super(MESSAGE);
    }

}
