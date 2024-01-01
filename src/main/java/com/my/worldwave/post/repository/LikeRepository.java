package com.my.worldwave.post.repository;

import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.dto.response.PostSummaryResponse;
import com.my.worldwave.post.entity.Like;
import com.my.worldwave.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByMemberAndPost(Member member, Post post);

    Optional<Like> findByMemberAndPost(Member member, Post post);

    int countByPost(Post post);

    // 게시글들에 해당하는 좋아요 삭제
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Like l WHERE l.post IN :posts")
    void deleteAllLikesByPosts(List<Post> posts);

}
