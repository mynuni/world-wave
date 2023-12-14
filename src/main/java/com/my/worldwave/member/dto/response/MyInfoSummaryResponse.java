package com.my.worldwave.member.dto.response;

import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.entity.RegisterType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyInfoSummaryResponse {
    private Long id;
    private String email;
    private String nickname;
    private String profileImgPath;
    private RegisterType registerType;

    @Builder
    public MyInfoSummaryResponse(Long id, String email, String nickname, String profileImgPath, RegisterType registerType) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImgPath = profileImgPath;
        this.registerType = registerType;
    }

    public static MyInfoSummaryResponse from(Member member) {
        MyInfoSummaryResponseBuilder builder = MyInfoSummaryResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .registerType(member.getRegisterType());

        if (member.getProfileImage() != null && member.getProfileImage().getStoredFileName() != null) {
            builder.profileImgPath(member.getProfileImage().getStoredFileName());
        }

        return builder.build();
    }

}
