package com.my.worldwave.post.service;

import com.my.worldwave.post.dto.CommentRequestDto;
import com.my.worldwave.post.dto.CommentResponseDto;
import com.my.worldwave.post.entity.Comment;
import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.repository.CommentRepository;
import com.my.worldwave.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.my.worldwave.post.dto.CommentResponseDto.convertToDto;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public CommentResponseDto findById(Long id) {
        Comment foundComment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT NOT FOUND. ID:" + id));

        return convertToDto(foundComment);
    }

    public Long createComment(Long postId, CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND ID:" + postId));

        Comment newComment = Comment.builder()
                .content(commentRequestDto.getContent())
                .author(commentRequestDto.getAuthor())
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(newComment);
        return savedComment.getId();
    }

    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment foundComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT NOT FOUND ID:" + commentId));

        foundComment.updateEntity(commentRequestDto.getContent());
        return convertToDto(foundComment);
    }

    public void deleteComment(Long id) {
        Comment foundComment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT NOT FOUND. ID:" + id));

        commentRepository.delete(foundComment);
    }

}