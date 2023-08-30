package com.my.worldwave.member.repository;

import com.my.worldwave.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    List<Member> findAllByCountry(String country);

    /**
     * 친구 추천: 국가 코드로 필터링 후 본인은 제외
     * 추가적으로 나를 팔로우한 사람, 친구의 친구 조회 기능 추가할 것
     * @param myId 본인을 제외하기 위한 ID
     * @param country 필터링을 위한 키워드
     */
    @Query("SELECT m FROM Member m " +
           "WHERE m.country = :country AND m.id <> :myId")
    Page<Member> findSuggestedMembers(@Param("myId") Long myId, @Param("country") String country, Pageable pageable);

}
