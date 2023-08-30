package com.my.worldwave.member.dto;

import com.my.worldwave.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowResponse {
    private Long friendId;
    private String profileImgPath;
    private String nickname;
    private boolean isAlreadyFollowed;

    @Builder
    public FollowResponse(Long friendId, String profileImgPath, String nickname, boolean isAlreadyFollowed) {
        this.friendId = friendId;
        this.profileImgPath = profileImgPath;
        this.nickname = nickname;
        this.isAlreadyFollowed = isAlreadyFollowed;
    }

    public FollowResponse(boolean isAlreadyFollowed) {
        this.isAlreadyFollowed = isAlreadyFollowed;
    }

    public static FollowResponse toDto(Member follower) {
        return FollowResponse.builder()
                .friendId(follower.getId())
                .profileImgPath(follower.getProfileImg().getFilePath())
                .nickname(follower.getNickname())
                .isAlreadyFollowed(true)
                .build();
    }

}
