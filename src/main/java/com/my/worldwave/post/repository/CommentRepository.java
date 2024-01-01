package com.my.worldwave.post.repository;


import com.my.worldwave.post.entity.Comment;
import com.my.worldwave.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByPostId(Long postId, Pageable pageable);

    Page<Comment> findAllByAuthorId(Long memberId, Pageable pageable);

    List<Comment> findByIdIn(List<Long> commentIds);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.author.id = :memberId AND c.id IN :commentIds")
    void deleteByAuthorIdAndIdIn(@Param("memberId") Long memberId, @Param("commentIds") List<Long> commentIds);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Comment c WHERE c.post IN :posts")
    void deleteAllCommentsByPosts(List<Post> posts);

}
