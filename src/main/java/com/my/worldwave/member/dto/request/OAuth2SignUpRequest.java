package com.my.worldwave.member.dto.request;

import com.my.worldwave.member.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.my.worldwave.exception.ValidationConstants.*;

@Getter
@Setter
@NoArgsConstructor
public class OAuth2SignUpRequest {
    @NotBlank(message = EMPTY_EMAIL_TOKEN_MESSAGE)
    private String emailToken;

    @Size(min = 2, max = 10, message = INVALID_NICKNAME_FORMAT)
    @NotBlank(message = EMPTY_NICKNAME_MESSAGE)
    private String nickname;

    @Size(min = 2, max = 2, message = INVALID_COUNTRY_FORMAT)
    @NotBlank(message = EMPTY_COUNTRY_MESSAGE)
    private String country;

    @NotNull(message = EMPTY_GENDER_MESSAGE)
    private Gender gender;

    @NotNull(message = EMPTY_AGE_MESSAGE)
    private Integer ageRange;

}
