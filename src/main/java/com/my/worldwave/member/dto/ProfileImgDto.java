package com.my.worldwave.member.dto;

import com.my.worldwave.member.entity.ProfileImg;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ProfileImgDto {
    private String originalFileName;
    private String filePath;

    @Builder
    public ProfileImgDto(String originalFileName, String filePath) {
        this.originalFileName = originalFileName;
        this.filePath = filePath;
    }

    public static ProfileImgDto toDto(ProfileImg profileImg) {
        return ProfileImgDto.builder()
                .originalFileName(profileImg.getOriginalFileName())
                .filePath(profileImg.getFilePath())
                .build();
    }

}
