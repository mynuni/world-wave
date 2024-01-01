package com.my.worldwave.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
public class FollowResponse {
    private Long memberId;
    private String nickname;
    private String country;
    private String profileImage;
    private boolean isFollowed;

    @Builder
    public FollowResponse(Long memberId, String nickname, String country, String profileImage, boolean isFollowed) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.country = country;
        this.profileImage = profileImage;
        this.isFollowed = isFollowed;
    }

    public FollowResponse(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

}
