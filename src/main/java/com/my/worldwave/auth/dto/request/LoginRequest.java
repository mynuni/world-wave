package com.my.worldwave.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.my.worldwave.exception.ValidationConstants.EMPTY_EMAIL_MESSAGE;
import static com.my.worldwave.exception.ValidationConstants.EMPTY_PASSWORD_MESSAGE;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = EMPTY_EMAIL_MESSAGE)
    private String email;

    @NotBlank(message = EMPTY_PASSWORD_MESSAGE)
    private String password;

}
