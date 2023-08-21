package com.my.worldwave.post.dto;

import com.my.worldwave.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.my.worldwave.post.dto.CommentResponseDto.convertToDtoList;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorNickname;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private List<CommentResponseDto> comments;
    private int commentCount;

    public static PostResponseDto convertToDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthor().getId())
                .authorNickname(post.getAuthor().getNickname())
                .country(post.getCountry())
                .createdAt(post.getCreatedAt())
                .lastUpdatedAt(post.getLastUpdatedAt())
                .comments(convertToDtoList(post.getComments()))
                .commentCount(post.getComments().size())
                .build();
    }

}
