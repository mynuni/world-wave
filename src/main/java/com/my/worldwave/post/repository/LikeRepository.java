package com.my.worldwave.post.repository;

import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.entity.Like;
import com.my.worldwave.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByMemberAndPost(Member member, Post post);

    Optional<Like> findByMemberAndPost(Member member, Post post);

}
