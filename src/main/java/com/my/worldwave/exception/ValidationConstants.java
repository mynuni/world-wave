package com.my.worldwave.exception;

public class ValidationConstants {

    public static final String EMPTY_EMAIL_MESSAGE = "이메일을 입력해주세요.";
    public static final String EMPTY_PASSWORD_MESSAGE = "비밀번호를 입력해주세요.";
    public static final String EMPTY_NICKNAME_MESSAGE = "닉네임을 입력해주세요.";
    public static final String EMPTY_COUNTRY_MESSAGE = "국가를 선택해주세요.";

    public static final String EMAIL_POLICY_MESSAGE = "올바른 이메일 형식이 아닙니다.";
    public static final String PASSWORD_POLICY_MESSAGE = "비밀번호는 영문+숫자 조합 8자~20자여야 합니다.";
    public static final String NICKNAME_POLICY_MESSAGE = "닉네임은 한글 또는 영문 2자~20자여야 합니다.";
    public static final String COUNTRY_POLICY_MESSAGE = "올바르지 않은 국가코드입니다.";

    public static final String NICKNAME_REGEX = "^[A-Za-z가-힣0-9]{2,20}$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$";
    public static final String COUNTRY_REGEX = "^[A-Za-z]{2}$";

}
