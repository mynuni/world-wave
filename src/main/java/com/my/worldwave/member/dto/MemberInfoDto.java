package com.my.worldwave.member.dto;

import com.my.worldwave.member.domain.Member;
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

    @Builder
    public MemberInfoDto(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static MemberInfoDto convertToDto(Member member) {
        return MemberInfoDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}
