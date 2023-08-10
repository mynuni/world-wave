package com.my.worldwave.exception.member;

public class DuplicateNicknameException extends DuplicateValueException {

    public DuplicateNicknameException() {
        super("닉네임");
    }

}
