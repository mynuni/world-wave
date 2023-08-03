package com.my.worldwave.post.repository;

import com.my.worldwave.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "select p from Post p left join fetch p.comments", countQuery = "select count(p) from Post p")
    Page<Post> findAllPosts(Pageable pageable);

    @Query(value = "SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.country = :country",
            countQuery = "SELECT COUNT(p) FROM Post p WHERE p.country = :country")
    Page<Post> findAllPostsByCountry(@Param("country") String country, Pageable pageable);

}