package com.my.worldwave.exception.member;

import com.my.worldwave.exception.BadRequestException;

public class PasswordMismatchException extends BadRequestException {

    private static final String MESSAGE = "비밀번호 확인이 일치하지 않습니다.";

    public PasswordMismatchException() {
        super(MESSAGE);
    }

}
