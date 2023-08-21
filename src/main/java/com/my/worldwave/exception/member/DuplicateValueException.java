package com.my.worldwave.exception.member;

import com.my.worldwave.exception.BadRequestException;

public class DuplicateValueException extends BadRequestException {

    private static final String DUPLICATE_MESSAGE_TEMPLATE = "이미 존재하는 %s입니다.";

    public DuplicateValueException(String value) {
        super(String.format(DUPLICATE_MESSAGE_TEMPLATE, value));
    }

}
