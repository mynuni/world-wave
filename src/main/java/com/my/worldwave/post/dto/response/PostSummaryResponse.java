package com.my.worldwave.post.dto.response;

import com.my.worldwave.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostSummaryResponse {
    private Long postId;
    private Long authorId;
    private String authorNickname;
    private String authorProfileImage;
    private String thumbnail;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private boolean isLiked;

    @Builder
    public PostSummaryResponse(Long postId, Long authorId, String authorNickname, String authorProfileImage, String thumbnail, int likeCount, int commentCount, LocalDateTime createdAt, boolean isLiked) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.authorProfileImage = authorProfileImage;
        this.thumbnail = thumbnail;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.isLiked = isLiked;
    }

    public static PostSummaryResponse of(Post post, Long currentMemberId) {
        return PostSummaryResponse.builder()
                .postId(post.getId())
                .authorId(post.getAuthor().getId())
                .authorNickname(post.getAuthor().getNickname())
                .authorProfileImage(post.getAuthor().getProfileImage().getStoredFileName())
                .thumbnail(post.getFiles().isEmpty() ? null : post.getFiles().get(0).getStoredFileName())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .isLiked(post.getLikes().stream().anyMatch(like -> like.getMember().getId().equals(currentMemberId)))
                .build();
    }

}
