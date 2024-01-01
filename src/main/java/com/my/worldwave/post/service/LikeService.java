package com.my.worldwave.post.service;

import com.my.worldwave.event.LikeEvent;
import com.my.worldwave.exception.post.PostNotFoundException;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.dto.response.LikeResponse;
import com.my.worldwave.post.entity.Like;
import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.repository.LikeRepository;
import com.my.worldwave.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public LikeResponse toggleLike(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);

        boolean isAlreadyLiked = isAlreadyLiked(postId, member);
        if (isAlreadyLiked) {
            deleteLike(member, post);
        } else {
            saveLike(member, post);
        }

        int likeCount = likeRepository.countByPost(post);
        return new LikeResponse(likeCount, !isAlreadyLiked);
    }

    public void saveLike(Member member, Post post) {
        likeRepository.save(new Like(member, post));
        eventPublisher.publishEvent(new LikeEvent(this, member, post.getAuthor(), post.getId()));
    }

    public void deleteLike(Member member, Post post) {
        Like like = likeRepository.findByMemberAndPost(member, post).orElseThrow(EntityNotFoundException::new);
        if (like != null) {
            likeRepository.delete(like);
        }
    }

    public boolean isAlreadyLiked(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
        return likeRepository.existsByMemberAndPost(member, post);
    }

}
