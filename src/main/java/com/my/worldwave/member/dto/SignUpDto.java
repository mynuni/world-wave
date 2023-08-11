package com.my.worldwave.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.my.worldwave.exception.ValidationConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    @NotBlank(message = EMPTY_EMAIL_MESSAGE)
    @Email(message = EMAIL_POLICY_MESSAGE)
    private String email;

    @NotBlank(message = EMPTY_PASSWORD_MESSAGE)
    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_POLICY_MESSAGE)
    private String password;

    @NotBlank(message = EMPTY_PASSWORD_MESSAGE)
    private String passwordCheck;

    @NotBlank(message = EMPTY_NICKNAME_MESSAGE)
    @Pattern(regexp = NICKNAME_REGEX, message = NICKNAME_POLICY_MESSAGE)
    private String nickname;

    @NotBlank(message = EMPTY_COUNTRY_MESSAGE)
    @Pattern(regexp = COUNTRY_REGEX, message = COUNTRY_POLICY_MESSAGE)
    private String country;
}
