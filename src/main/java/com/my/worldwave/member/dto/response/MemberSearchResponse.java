package com.my.worldwave.member.dto.response;

import com.my.worldwave.member.entity.Gender;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class MemberSearchResponse {
    private Long id;
    private String nickname;
    private String country;
    private Gender gender;
    private Integer ageRange;
    private long followerCount;
    private long followingCount;
    private boolean isFollowed;
    private String profileImage;

    public MemberSearchResponse(Long id, String nickname, String country, Gender gender, Integer ageRange, boolean isFollowed, String profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.country = country;
        this.gender = gender;
        this.ageRange = ageRange;
        this.isFollowed = isFollowed;
        this.profileImage = profileImage;
    }

    public MemberSearchResponse(Long id, String nickname, String country, Gender gender, Integer ageRange, long followerCount, long followingCount, boolean isFollowed, String profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.country = country;
        this.gender = gender;
        this.ageRange = ageRange;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.isFollowed = isFollowed;
        this.profileImage = profileImage;
    }

}
