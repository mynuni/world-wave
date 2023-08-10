package com.my.worldwave.exception.member;

public class DuplicateEmailException extends DuplicateValueException {

    private static final String VALUE = "이메일";

    public DuplicateEmailException() {
        super(VALUE);
    }

}
