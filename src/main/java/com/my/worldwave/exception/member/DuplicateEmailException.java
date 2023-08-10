package com.my.worldwave.exception.member;

public class DuplicateEmailException extends DuplicateValueException {

    public DuplicateEmailException() {
        super("이메일");
    }

}
