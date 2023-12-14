package com.my.worldwave.member.dto.request;

import com.my.worldwave.member.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.my.worldwave.exception.ValidationConstants.*;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest {

    @Pattern(regexp = REGEX_EMAIL, message = INVALID_EMAIL_FORMAT)
    @NotBlank(message = EMPTY_EMAIL_MESSAGE)
    private String email;

    @Pattern(regexp = REGEX_PASSWORD, message = INVALID_PASSWORD_FORMAT)
    @NotBlank(message = EMPTY_PASSWORD_MESSAGE)
    private String password;

    @NotBlank(message = EMPTY_PASSWORD_CONFIRM_MESSAGE)
    private String passwordConfirm;

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

    @Override
    public String toString() {
        return "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                ", nickname='" + nickname + '\'' +
                ", country='" + country + '\'' +
                ", gender=" + gender +
                ", ageRange=" + ageRange;
    }

}
