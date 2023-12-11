package com.my.worldwave.exception;

public class ValidationConstants {

    public static final String EMPTY_EMAIL_MESSAGE = "이메일을 입력해주세요.";
    public static final String EMPTY_PASSWORD_MESSAGE = "비밀번호를 입력해주세요.";
    public static final String EMPTY_PASSWORD_CONFIRM_MESSAGE = "비밀번호 확인을 입력해주세요.";
    public static final String EMPTY_NICKNAME_MESSAGE = "닉네임을 입력해주세요.";
    public static final String EMPTY_COUNTRY_MESSAGE = "국가를 선택해주세요.";
    public static final String EMPTY_GENDER_MESSAGE = "성별을 선택해주세요.";
    public static final String EMPTY_AGE_MESSAGE = "나이를 선택해주세요.";
    public static final String EMPTY_EMAIL_TOKEN_MESSAGE = "OAuth2 인증 정보가 올바르지 않습니다. 다시 로그인 해주세요.";

    public static final String INVALID_EMAIL_FORMAT = "올바른 이메일 형식이 아닙니다.";
    public static final String INVALID_PASSWORD_FORMAT = "8~20자의 영문, 숫자 조합을 사용해주세요.";
    public static final String INVALID_NICKNAME_FORMAT = "닉네임은 한글 또는 영문 2자~20자여야 합니다.";
    public static final String INVALID_COUNTRY_FORMAT = "올바르지 않은 국가코드입니다.";

    public static final String REGEX_EMAIL = "^[a-zA-Z0-9+-.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$";
    public static final String REGEX_PASSWORD = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}$";

}
