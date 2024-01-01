package com.my.worldwave.post.repository;

import com.my.worldwave.post.entity.Post;
import com.my.worldwave.post.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE PostFile pf SET pf.isDeleted = true WHERE pf.post IN :posts")
    void deleteAllByPosts(List<Post> posts);

}
