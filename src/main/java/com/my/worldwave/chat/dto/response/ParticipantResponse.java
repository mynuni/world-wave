package com.my.worldwave.chat.dto.response;

import com.my.worldwave.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ParticipantResponse {
    private Long id;
    private String nickname;
    private String profileImage;

    @Builder
    public ParticipantResponse(Long id, String nickname, String profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static ParticipantResponse from(Member member) {
        return ParticipantResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage().getStoredFileName())
                .build();
    }

}
