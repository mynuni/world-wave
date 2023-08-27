package com.my.worldwave.post.repository;

import com.my.worldwave.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT p FROM Post p " +
                   "LEFT JOIN FETCH p.comments",
            countQuery = "SELECT COUNT(p) FROM Post p")
    Page<Post> findAllPosts(Pageable pageable);

    // 검색과 정렬 조건 동시 적용
    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.comments " +
            "WHERE p.country = :country " +
            "AND (:content IS NULL OR p.content LIKE %:content%) " +
            "ORDER BY CASE WHEN :sortCond = 'likes' THEN p.likesCount END DESC, " +
                     "CASE WHEN :sortCond = 'comments' THEN p.commentsCount END DESC, " +
                     "p.createdAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "WHERE p.country = :country " +
                    "AND (:content IS NULL OR p.content LIKE %:content%)")
    Page<Post> findAllContentContainingWithSorting(@Param("country") String country,
                                                   @Param("content") String searchKeyword,
                                                   @Param("sortCond") String sortCond, Pageable pageable);

    // 검색만 분리
    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.comments " +
            "WHERE p.country = :country " +
            "AND (:content IS NULL OR p.content LIKE %:content%) " +
            "ORDER BY p.createdAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "WHERE p.country = :country " +
                    "AND (:content IS NULL OR p.content LIKE %:content%)")
    Page<Post> findAllContentContaining(@Param("country") String country, @Param("content") String searchKeyword, Pageable pageable);

    // 정렬만 분리
    @Query(value = "SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.comments " +
            "WHERE p.country = :country " +
            "ORDER BY CASE WHEN :sortCond = 'likes' THEN p.likesCount END DESC, " +
            "CASE WHEN :sortCond = 'comments' THEN p.commentsCount END DESC, " +
            "p.createdAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "WHERE p.country = :country ")
    Page<Post> findAllWithSort(@Param("country") String country, @Param("sortCond") String sortCond, Pageable pageable);

    // 좋아요순 정렬 조건 분리
    @Query(value = "SELECT p FROM Post p " +
                   "LEFT JOIN FETCH p.comments " +
                   "WHERE p.country = :country " +
                   "ORDER BY p.likesCount DESC, p.createdAt desc",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                         "WHERE p.country = :country " +
                         "GROUP BY p.id")
    Page<Post> findAllOrderByLikes(@Param("country") String country, Pageable pageable);

    // 댓글수 정렬 조건 분리
    @Query(value = "SELECT p FROM Post p " +
                   "LEFT JOIN FETCH p.comments " +
                   "WHERE p.country = :country " +
                   "ORDER BY p.commentsCount DESC, p.createdAt desc",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                         "WHERE p.country = :country " +
                         "GROUP BY p.id ")
    Page<Post> findAllOrderByComments(@Param("country") String country, Pageable pageable);

}
