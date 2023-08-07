package com.my.worldwave.member.dto;

import com.my.worldwave.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfoDto implements Serializable {
    private Long id;
    private String email;
    private String nickname;

    // 기타 추가 정보 등

    @Builder
    public MemberInfoDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public static MemberInfoDto convertToDto(Member member) {
        return MemberInfoDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
