package com.my.worldwave.post.repository;

import com.my.worldwave.member.entity.Member;
import com.my.worldwave.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 팔로우 중인 사용자와 본인의 게시글 목록을 조회
    @Query(value = "SELECT p FROM Post p " +
                   "JOIN FETCH p.author " +
                   "WHERE p.author.id = :currentMemberId " +
                   "OR p.author.id IN (SELECT f.following.id FROM Follow f " +
                                      "WHERE f.follower.id = :currentMemberId) ",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                         "WHERE p.author.id = :currentMemberId " +
                         "OR p.author.id IN (SELECT f.following.id FROM Follow f " +
                                            "WHERE f.follower.id = :currentMemberId)")
    Page<Post> findAllWithFilesAndLikes(@Param("currentMemberId") Long currentMemberId, Pageable pageable);

    // 게시글 단건 조회
    @Query(value = "SELECT p FROM Post p " +
                   "JOIN FETCH p.author " +
                   "WHERE p.id = :id")
    Optional<Post> getPostByIdWithFilesAndLikes(Long id);

    // 특정 사용자가 작성한 게시글 목록 조회
    @Query(value = "SELECT p FROM Post p " +
            "JOIN FETCH p.author m " +
            "WHERE m.id = :authorId ",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                         "JOIN p.author m " +
                         "WHERE m.id = :authorId")
    Page<Post> findPostsByAuthorId(@Param("authorId") Long authorId, Pageable pageable);

    // 특정 사용자가 좋아요한 게시글 목록
    @Query(value = "SELECT p FROM Post p " +
            "JOIN FETCH p.author " +
            "JOIN FETCH p.likes l " +
            "WHERE l.member.id = :memberId " +
            "ORDER BY l.createdAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "JOIN p.likes l " +
                    "WHERE l.member.id = :memberId")
    Page<Post> findAllByLikedMemberId(Long memberId, Pageable pageable);

    List<Post> findAllByAuthor(@Param("author") Member member);

}
