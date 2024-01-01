package com.my.worldwave.post.dto.response;

import lombok.Getter;

@Getter
public class LikeResponse {
    int likeCount;
    boolean isAlreadyLiked;

    public LikeResponse(int likeCount, boolean isAlreadyLiked) {
        this.likeCount = likeCount;
        this.isAlreadyLiked = isAlreadyLiked;
    }

}
