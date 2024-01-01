package com.my.worldwave.post.dto.response;

import com.my.worldwave.post.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private Long postId;
    private Long authorId;
    private String authorNickname;
    private String authorProfileImage;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    @Builder
    public CommentResponse(Long id, Long postId, Long authorId, String authorNickname, String authorProfileImage, String content, LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.authorProfileImage = authorProfileImage;
        this.content = content;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .authorId(comment.getAuthor().getId())
                .authorNickname(comment.getAuthor().getNickname())
                .authorProfileImage(comment.getAuthor().getProfileImage().getStoredFileName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .lastUpdatedAt(comment.getLastUpdatedAt())
                .build();
    }

}
