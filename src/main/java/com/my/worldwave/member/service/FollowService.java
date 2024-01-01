package com.my.worldwave.member.service;

import com.my.worldwave.event.FollowEvent;
import com.my.worldwave.member.dto.response.FollowResponse;
import com.my.worldwave.member.entity.Follow;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional
@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public Page<FollowResponse> getFollowers(Long targetMemberId, Long currentMemberId, Pageable pageable) {
        return followRepository.findFollowers(targetMemberId, currentMemberId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<FollowResponse> getFollowingMembers(Long targetId, Long currentMemberId, PageRequest pageRequest) {
        return followRepository.findFollowings(targetId, currentMemberId, pageRequest);
    }

    /**
     * isAlreadyFollowed 상태에 따른 토글 방식의 팔로우/언팔로우 메서드
     *
     * @param follower  팔로우 동작을 하는 주체
     * @param following 팔로우를 당하는 대상
     */
    @Transactional
    public FollowResponse toggleFollow(Member follower, Member following) {
        boolean isAlreadyFollowed = isAlreadyFollowed(follower, following);
        if (isAlreadyFollowed) {
            unfollow(follower, following);
        } else {
            follow(follower, following);
            eventPublisher.publishEvent(new FollowEvent(this, follower, following));
        }

        return new FollowResponse(!isAlreadyFollowed);
    }

    /**
     * 이미 팔로우 중인지 확인하는 메서드
     * follower:followee pair는 unique한 하나의 레코드로 구성
     */
    private boolean isAlreadyFollowed(Member follower, Member following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    private void follow(Member follower, Member followee) {
        followRepository.save(new Follow(follower, followee));
    }

    private void unfollow(Member follower, Member following) {
        Follow follow = followRepository.findByFollowerAndFollowing(follower, following).orElseThrow(EntityNotFoundException::new);
        followRepository.delete(follow);
    }

}