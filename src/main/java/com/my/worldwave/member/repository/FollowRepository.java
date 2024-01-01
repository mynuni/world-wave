package com.my.worldwave.member.repository;

import com.my.worldwave.member.dto.response.FollowResponse;
import com.my.worldwave.member.entity.Follow;
import com.my.worldwave.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(Member follower, Member following);

    Optional<Follow> findByFollowerAndFollowing(Member follower, Member following);

    /**
     * 특정 회원을 팔로우 하고 있는 회원 목록을 조회
     *
     * @param targetId  조회 대상 회원 ID
     * @param myId 현재 로그인한 회원 ID
     * @return 회원 정보, 회원 사진, 나의 팔로우 여부 반환
     */
    @Query(value = "SELECT new com.my.worldwave.member.dto.response.FollowResponse(" +
            "f.follower.id, f.follower.nickname, f.follower.country, f.follower.profileImage.storedFileName, " +
            "CASE WHEN EXISTS (SELECT 1 FROM Follow sub WHERE sub.follower.id = :myId AND sub.following.id = f.follower.id) THEN true ELSE false END) " +
            "FROM Follow f " +
            "LEFT JOIN f.follower.profileImage " +
            "WHERE f.following.id = :targetId", countQuery = "SELECT COUNT(f) FROM Follow f WHERE f.following.id = :targetId")
    Page<FollowResponse> findFollowers(@Param("targetId") Long targetId, @Param("myId") Long myId, Pageable pageable);

    /**
     * 특정 회원이 팔로우 중인 회원 목록 조회
     */
    @Query(value = "SELECT new com.my.worldwave.member.dto.response.FollowResponse(" +
            "f.following.id, f.following.nickname, f.following.country, f.following.profileImage.storedFileName, " +
            "CASE WHEN EXISTS (SELECT 1 FROM Follow sub WHERE sub.follower.id = :myId AND sub.following.id = f.following.id) THEN true ELSE false END) " +
            "FROM Follow f " +
            "LEFT JOIN f.following.profileImage " +
            "WHERE f.follower.id = :targetId", countQuery = "SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :targetId")
    Page<FollowResponse> findFollowings(@Param("targetId") Long targetId, @Param("myId") Long myId, Pageable pageable);

}
