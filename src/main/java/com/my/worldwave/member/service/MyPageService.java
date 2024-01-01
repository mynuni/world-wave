package com.my.worldwave.member.service;

import com.my.worldwave.post.dto.response.CommentResponse;
import com.my.worldwave.post.dto.response.PostSummaryResponse;
import com.my.worldwave.post.entity.Comment;
import com.my.worldwave.post.repository.CommentRepository;
import com.my.worldwave.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<CommentResponse> getMyComments(Long memberId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findAllByAuthorId(memberId, pageable);
        return comments.map(CommentResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getMyLikes(Long memberId, Pageable pageable) {
        return postRepository.findAllByLikedMemberId(memberId, pageable)
                .map(post -> PostSummaryResponse.of(post, memberId));
    }

    // 다중 댓글 삭제
    @Transactional
    public void deleteMyComments(List<Long> commentIds, Long memberId) {
        List<Comment> comments = commentRepository.findByIdIn(commentIds);
        comments.forEach(comment -> {
            if (!comment.getAuthor().getId().equals(memberId)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
        });

        commentRepository.deleteByAuthorIdAndIdIn(memberId, commentIds);
    }

}
