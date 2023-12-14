package com.my.worldwave.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.my.worldwave.exception.ValidationConstants.EMPTY_EMAIL_MESSAGE;

@Getter
@Setter
@NoArgsConstructor
public class PasswordResetRequest {

    @NotBlank(message = EMPTY_EMAIL_MESSAGE)
    private String email;
    private String password;
    private String passwordConfirm;
    private String passwordChangeToken;

}
