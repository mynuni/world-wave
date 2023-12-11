package com.my.worldwave.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.my.worldwave.exception.ValidationConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequest {

    @Pattern(regexp = REGEX_EMAIL, message = INVALID_EMAIL_FORMAT)
    @NotBlank(message = EMPTY_EMAIL_MESSAGE)
    private String email;

    private String verificationCode;

    public EmailVerificationRequest(String email) {
        this.email = email;
    }

}