package com.my.worldwave.exception.auth;

public class EmailSendingFailException extends RuntimeException {

    private static final String message = "이메일 발송에 실패했습니다.";

    public EmailSendingFailException() {
        super(message);
    }

    public EmailSendingFailException(String message) {
        super(message);
    }
}
