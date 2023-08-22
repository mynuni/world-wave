package com.my.worldwave.exception.member;

public class RefreshTokenExpiredException extends RuntimeException {

    private static final String MESSAGE = "로그인이 필요합니다.";

    public RefreshTokenExpiredException() {
        super(MESSAGE);
    }

}
