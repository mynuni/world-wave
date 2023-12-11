package com.my.worldwave.exception.member;

import com.my.worldwave.exception.BadRequestException;

public class PasswordConfirmMismatchException extends BadRequestException {

    private static final String message = "비밀번호 확인이 일치하지 않습니다.";

    public PasswordConfirmMismatchException() {
        super(message);
    }
}
