package com.my.worldwave.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private String email;
    private String password;
    private String passwordCheck;
    private String nickname;
    private String country;
}
