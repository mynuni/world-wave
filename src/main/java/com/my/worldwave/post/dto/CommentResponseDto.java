package com.my.worldwave.post.dto.response;

import com.my.worldwave.post.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private Long postId;
    private String content;
    private Long authorId;
    private String authorNickname;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private boolean hasPermission;
    private String profileImgPath;

    public static List<CommentDto> convertToDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentDto::toDto)
                .collect(Collectors.toList());
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .authorId(comment.getAuthor().getId())
                .authorNickname(comment.getAuthor().getNickname())
                .createdAt(comment.getCreatedAt())
                .lastUpdatedAt(comment.getLastUpdatedAt())
                .hasPermission(false)
                .build();
    }

}
