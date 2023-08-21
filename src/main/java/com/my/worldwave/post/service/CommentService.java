package com.my.worldwave.post.service;

import com.my.worldwave.exception.post.CommentNotFoundException;
import com.my.worldwave.exception.post.PostNotFoundException;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.dto.CommentRequestDto;
import com.my.worldwave.post.dto.CommentResponseDto;
import com.my.worldwave.post.entity.Comment;
import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.repository.CommentRepository;
import com.my.worldwave.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.my.worldwave.post.dto.CommentResponseDto.convertToDto;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    public Long createComment(Long postId, Member member, CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Comment newComment = Comment.builder()
                .content(commentRequestDto.getContent())
                .author(member)
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(newComment);
        return savedComment.getId();
    }

    public CommentResponseDto updateComment(Long commentId, Member member, CommentRequestDto commentRequestDto) {
        Comment foundComment = findById(commentId);
        checkAuthority(member, foundComment);
        foundComment.updateEntity(commentRequestDto.getContent());
        return convertToDto(foundComment);
    }

    public void deleteComment(Long id, Member member) {
        Comment foundComment = findById(id);
        checkAuthority(member, foundComment);
        commentRepository.delete(foundComment);
    }

    private void checkAuthority(Member member, Comment comment) {
        if (!comment.getAuthor().getId().equals(member.getId())) {
            throw new AccessDeniedException("ACCESS DENIED");
        }
    }

}