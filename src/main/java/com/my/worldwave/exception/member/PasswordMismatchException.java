package com.my.worldwave.exception.member;

import com.my.worldwave.exception.BadRequestException;

public class PasswordMismatchException extends BadRequestException {

    private static final String MESSAGE = "인증 정보가 올바르지 않습니다.";

    public PasswordMismatchException() {
        super(MESSAGE);
    }

}
