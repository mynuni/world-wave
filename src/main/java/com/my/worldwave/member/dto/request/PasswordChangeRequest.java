package com.my.worldwave.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.my.worldwave.exception.ValidationConstants.*;

@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeRequest {

    @NotBlank(message = EMPTY_PASSWORD_MESSAGE)
    private String currentPw;

    @Pattern(regexp = REGEX_PASSWORD, message = INVALID_PASSWORD_FORMAT)
    @NotBlank(message = EMPTY_PASSWORD_MESSAGE)
    private String newPw;

    @NotBlank(message = EMPTY_PASSWORD_CONFIRM_MESSAGE)
    private String newPwConfirm;

}
