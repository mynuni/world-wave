package com.my.worldwave.member.dto.request;

import com.my.worldwave.member.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequest {
    private String nickname;
    private Integer ageRange;
    private Gender gender;
    private String country;

}
