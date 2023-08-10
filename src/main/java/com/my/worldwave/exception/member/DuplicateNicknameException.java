package com.my.worldwave.exception.member;

public class DuplicateNicknameException extends DuplicateValueException {

    private static final String VALUE = "닉네임";

    public DuplicateNicknameException() {
        super(VALUE);
    }

}
