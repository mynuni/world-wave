package com.my.worldwave.member.repository;

import com.my.worldwave.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findByEmail(String email);

    public boolean existsByEmail(String email);

    public boolean existsByNickname(String nickname);

}
