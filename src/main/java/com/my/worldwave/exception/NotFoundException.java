package com.my.worldwave.exception;

public class NotFoundException extends RuntimeException {

    private static final String NOT_FOUND_MESSAGE_TEMPLATE = "존재하지 않는 %s입니다. ID:%d";

    public NotFoundException(String message, Long id) {
        super(String.format(NOT_FOUND_MESSAGE_TEMPLATE, message, id));
    }

}
