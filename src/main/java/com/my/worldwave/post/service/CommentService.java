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
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public CommentResponseDto findById(Long id) {
        Comment foundComment = findCommentById(id);
        return convertToDto(foundComment);
    }

    @Transactional
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

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment foundComment = findCommentById(commentId);
        foundComment.updateEntity(commentRequestDto.getContent());
        return convertToDto(foundComment);
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment foundComment = findCommentById(id);
        commentRepository.delete(foundComment);
    }

    @Transactional(readOnly = true)
    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("COMMENT NOT FOUND ID:" + id));
    }

}