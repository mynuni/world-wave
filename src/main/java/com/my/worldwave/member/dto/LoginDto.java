package com.my.worldwave.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static com.my.worldwave.exception.ValidationConstants.*;

@Getter
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = EMPTY_EMAIL_MESSAGE)
    @Email(message = EMAIL_POLICY_MESSAGE)
    private String email;

    @NotBlank(message = EMPTY_PASSWORD_MESSAGE)
    private String password;
}
