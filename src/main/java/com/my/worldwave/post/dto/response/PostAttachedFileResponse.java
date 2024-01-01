package com.my.worldwave.post.dto.response;

import com.my.worldwave.post.entity.PostFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostAttachedFileResponse {
    private Long id;
    private String storedFileName;

    public PostAttachedFileResponse(Long id, String storedFileName) {
        this.id = id;
        this.storedFileName = storedFileName;
    }

    public static PostAttachedFileResponse from(PostFile postFile) {
        return new PostAttachedFileResponse(postFile.getId(), postFile.getStoredFileName());
    }

}
