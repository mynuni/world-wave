package com.my.worldwave.post.dto;

import com.my.worldwave.post.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long id;
    private Long postId;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

//    public CommentResponseDto(Comment comment) {
//        this.id = comment.getId();
//        this.postId = comment.getPost().getId();
//        this.content = comment.getContent();
//        this.author = comment.getAuthor();
//        this.createdAt = comment.getCreatedAt();
//        this.lastUpdatedAt = comment.getLastUpdatedAt();
//    }

    public static List<CommentResponseDto> convertToDtoList(List<Comment> comments) {
        return comments.stream()
                .map(comment -> convertToDto(comment))
                .collect(Collectors.toList());
    }

    public static CommentResponseDto convertToDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .author(comment.getAuthor())
                .createdAt(comment.getCreatedAt())
                .lastUpdatedAt(comment.getLastUpdatedAt())
                .build();
    }

}
