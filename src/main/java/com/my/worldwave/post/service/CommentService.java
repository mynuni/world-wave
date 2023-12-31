package com.my.worldwave.post.service;

import com.my.worldwave.event.CommentEvent;
import com.my.worldwave.exception.post.CommentNotFoundException;
import com.my.worldwave.exception.post.PostNotFoundException;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.dto.request.CommentRequest;
import com.my.worldwave.post.dto.response.CommentDto;
import com.my.worldwave.post.dto.request.PostSearchRequest;
import com.my.worldwave.post.dto.response.CommentResponse;
import com.my.worldwave.post.entity.Comment;
import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.repository.CommentRepository;
import com.my.worldwave.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.my.worldwave.post.dto.response.CommentDto.toDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public Page<CommentResponse> findAll(Long postId, PostSearchRequest feedRequest) {
        Page<Comment> allByPostId = commentRepository.findAllByPostId(postId, feedRequest.toPageable());
        return commentRepository.findAllByPostId(postId, feedRequest.toPageable()).map(CommentResponse::from);
    }

    @Transactional(readOnly = true)
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
    }

    @Transactional
    public CommentDto createComment(Long postId, Member member, CommentRequest commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        Comment newComment = Comment.builder()
                .content(commentRequest.getContent())
                .author(member)
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(newComment);
        eventPublisher.publishEvent(new CommentEvent(this, member, post.getAuthor(), postId));
        return toDto(savedComment);
    }

    @Transactional
    public CommentDto updateComment(Long commentId, Member member, CommentRequest commentRequest) {
        Comment foundComment = findById(commentId);
        checkAuthority(member, foundComment);
        foundComment.updateContent(commentRequest.getContent());
        return toDto(foundComment);
    }

    @Transactional
    public void deleteComment(Long id, Member member) {
        Comment foundComment = findById(id);
        checkAuthority(member, foundComment);
        commentRepository.delete(foundComment);
    }

    private void checkAuthority(Member member, Comment comment) {
        if (!comment.getAuthor().getId().equals(member.getId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }

}