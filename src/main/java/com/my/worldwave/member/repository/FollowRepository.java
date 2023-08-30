package com.my.worldwave.member.repository;

import com.my.worldwave.member.entity.Follow;
import com.my.worldwave.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(Member follower, Member following);

    Optional<Follow> findByFollowerAndFollowing(Member follower, Member following);

    Page<Follow> findFollowsByFollowingId(Long memberId, Pageable pageable);

    Page<Follow> findFollowingByFollowerId(Long memberId, Pageable pageable);

}
