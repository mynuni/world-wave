package com.my.worldwave.post.dto.response;

import com.my.worldwave.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private Long authorId;
    private String authorNickname;
    private String profileImage;
    private String content;
    private List<PostAttachedFileResponse> attachedFiles;
    private int commentCount;
    private int likeCount;
    private boolean isLiked;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    @Builder
    public PostResponse(Long id, Long authorId, String authorNickname, String profileImg, String content, List<PostAttachedFileResponse> attachedFiles, int commentCount, int likeCount, boolean isLiked, LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.profileImage = profileImg;
        this.content = content;
        this.attachedFiles = attachedFiles;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public static PostResponse of(Post post, List<PostAttachedFileResponse> attachedFiles, boolean isLiked) {
        return PostResponse.builder()
                .id(post.getId())
                .authorId(post.getAuthor().getId())
                .authorNickname(post.getAuthor().getNickname())
                .profileImg(post.getAuthor().getProfileImage() != null ? post.getAuthor().getProfileImage().getStoredFileName() : null)
                .content(post.getContent())
                .attachedFiles(attachedFiles)
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .isLiked(isLiked)
                .createdAt(post.getCreatedAt())
                .lastUpdatedAt(post.getLastUpdatedAt())
                .build();
    }

//    public static PostResponse from(Post post) {
//        return PostResponse.builder()
//                .id(post.getId())
//                .authorId(post.getAuthor().getId())
//                .authorNickname(post.getAuthor().getNickname())
//                .profileImg(post.getAuthor().getProfileImg() != null ? post.getAuthor().getProfileImg().getStoredFileName() : null)
//                .content(post.getContent())
//                .attachedFiles(post.getFiles().stream()
//                        .map(PostAttachedFileResponse::from)
//                        .collect(Collectors.toList()))
//                .commentCount(post.getCommentCount())
//                .likeCount(post.getLikeCount())
//                .createdAt(post.getCreatedAt())
//                .lastUpdatedAt(post.getLastUpdatedAt())
//                .build();
//    }

}
