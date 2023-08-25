package com.my.worldwave.post.service;

import com.my.worldwave.exception.post.PostNotFoundException;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.dto.PostResponseDto;
import com.my.worldwave.post.entity.Like;
import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.repository.LikeRepository;
import com.my.worldwave.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostResponseDto toggleLike(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException());
        boolean isAlreadyLiked = isAlreadyLiked(postId, member);

        if (isAlreadyLiked) {
            log.info("좋아요 취소");
            deleteLike(member, post);
        } else {
            log.info("좋아요");
            saveLike(member, post);
        }
//        likeRepository.flush();
        PostResponseDto postResponseDto = PostResponseDto.convertToDto(post);
        postResponseDto.setLikeStatus(isAlreadyLiked);
        return postResponseDto;
    }

    @Transactional
    public void deleteLike(Member member, Post post) {
        Like like = likeRepository.findByMemberAndPost(member, post).orElseThrow(() -> new EntityNotFoundException());
        if (like != null) {
            likeRepository.delete(like);
        }
    }

    @Transactional
    public void saveLike(Member member, Post post) {
        Like newLike = new Like(member, post);
        likeRepository.save(newLike);
    }

    public boolean isAlreadyLiked(Long postId, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException());
        boolean status = likeRepository.existsByMemberAndPost(member, post);
        log.info("LIKE STATUS:{}", status);
        return status;
    }

}
