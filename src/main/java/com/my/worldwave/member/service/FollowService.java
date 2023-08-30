package com.my.worldwave.member.service;

import com.my.worldwave.member.dto.FollowResponse;
import com.my.worldwave.member.entity.Follow;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public List<FollowResponse> getFollowers(Long id, Pageable pageable) {
        Page<Follow> follows = followRepository.findFollowsByFollowingId(id, pageable);
        log.info("get size:{}", follows.getSize());
        return follows.getContent().stream()
                .map(follower -> FollowResponse.toDto(follower.getFollower()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getFollowingMembers(Long id, Pageable pageable) {
        Page<Follow> follows = followRepository.findFollowingByFollowerId(id, pageable);
        return follows.getContent().stream()
                .map(follower -> FollowResponse.toDto(follower.getFollower()))
                .collect(Collectors.toList());
    }

    /**
     * isAlreadyFollowed 상태에 따른 토글 방식의 팔로우/언팔로우 메서드
     *
     * @param follower  팔로우 동작을 하는 주체 ex)나
     * @param following 팔로우를 당하는 대상 ex)브래드 피트
     * @return 로직 수행 후 최종적으로 변경된 팔로우 상태를 반환
     */
    public FollowResponse toggleFollow(Member follower, Member following) {
        boolean isAlreadyFollowed = isAlreadyFollowed(follower, following);
        if (isAlreadyFollowed) {
            unfollow(follower, following);
        } else {
            follow(follower, following);
        }

        return new FollowResponse(!isAlreadyFollowed);
    }

    /**
     * 이미 팔로우 중인지 확인하는 메서드
     * follower:followee pair는 unique한 하나의 레코드로 구성
     *
     * @return 존재하면 true, 그렇지 않으면 false
     */
    @Transactional(readOnly = true)
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