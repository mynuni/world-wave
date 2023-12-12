package com.my.worldwave.post.entity;

import com.my.worldwave.member.entity.Member;
import com.my.worldwave.util.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "posts")
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @Formula("(SELECT COUNT(1) FROM comments c WHERE c.post_id = post_id)")
    private int commentCount;

    @Formula("(SELECT COUNT(1) FROM likes l WHERE l.post_id = post_id)")
    private int likeCount;

    @Builder
    public Post(String content, Member author) {
        this.content = content;
        this.author = author;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void removeAllFiles() {
        files.forEach(file -> file.setPost(null));
    }

}
