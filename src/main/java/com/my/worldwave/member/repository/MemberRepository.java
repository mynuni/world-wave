package com.my.worldwave.member.repository;

import com.my.worldwave.member.dto.response.MemberSearchResponse;
import com.my.worldwave.member.entity.Gender;
import com.my.worldwave.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 특정 회원의 정보와 팔로우 정보 조회
     */
    @Query("SELECT new com.my.worldwave.member.dto.response.MemberSearchResponse(" +
            "m.id, m.nickname, m.country, m.gender, m.ageRange, " +
            "(SELECT COUNT(follower) FROM Follow WHERE following.id = m.id), " +
            "(SELECT COUNT(follower) FROM Follow WHERE follower.id = m.id), " +
            "(CASE WHEN (" +
            "    SELECT COUNT(f) FROM Follow f " +
            "    WHERE f.follower.id = :myId AND f.following.id = m.id" +
            ") > 0 THEN true ELSE false END), " +
            "pi.storedFileName) " +
            "FROM Member m " +
            "LEFT JOIN m.profileImage pi " +
            "WHERE m.id = :memberId")
    Optional<MemberSearchResponse> findMemberWithFollowState(@Param("memberId") Long memberId, @Param("myId") Long myId);

    /**
     * 회원을 조건부로 검색
     */
    // AND 부분 쿼리 개선 필요
    @Query("SELECT new com.my.worldwave.member.dto.response.MemberSearchResponse(" +
            "m.id, m.nickname, m.country, m.gender, m.ageRange, " +
            "CASE WHEN f.id IS NOT NULL THEN true ELSE false END," +
            "pi.storedFileName ) " +
            "FROM Member m " +
            "LEFT JOIN m.profileImage pi " +
            "LEFT JOIN Follow f ON f.following.id = m.id AND f.follower.id = :currentMemberId " +
            "WHERE (:hideFollowers = false OR f.id IS NULL) " +
            "AND (:country IS NULL OR m.country = :country) " +
            "AND (:gender IS NULL OR m.gender = :gender) " +
            "AND (:ageRange IS NULL OR m.ageRange = :ageRange) " +
            "AND (:nickname IS NULL OR LOWER(m.nickname) LIKE LOWER(CONCAT(:nickname, '%')))" +
            "AND (m.isActivated = true) " +
            "AND m.id <> :currentMemberId")
    Page<MemberSearchResponse> searchMembers(
            @Param("currentMemberId") Long currentMemberId,
            @Param("country") String country,
            @Param("nickname") String nickname,
            @Param("gender") Gender gender,
            @Param("ageRange") Integer ageRange,
            @Param("hideFollowers") boolean hideFollowers,
            Pageable pageable);

    @Query("SELECT m FROM Member m " +
            "LEFT JOIN FETCH m.profileImage " +
            "WHERE m.id = :id")
    Optional<Member> findMemberWithProfileImg(@Param("id") Long id);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT m " +
           "FROM Member m " +
           "LEFT JOIN FETCH m.profileImage " +
           "WHERE m.id IN :memberIds")
    List<Member> findParticipantsIn(@Param("memberIds") List<Long> memberIds);

    /**
     * 회원 탈퇴 시 단방향 또는 Cascade 속성이 없는 연관 엔티티 벌크 삭제
     */
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Activity a WHERE a.actionMember = :member OR a.targetMember = :member")
    void deleteActivitiesByMember(Member member);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Follow f WHERE f.follower = :member OR f.following = :member")
    void deleteFollowsByMember(Member member);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Comment c WHERE c.author = :member")
    void deleteCommentsByMember(Member member);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Like l WHERE l.member = :member")
    void deleteLikesByMember(Member member);

}