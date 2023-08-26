package com.my.worldwave.post.entity;

import com.my.worldwave.member.entity.Member;
import com.my.worldwave.util.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "POSTS")
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false, length = 2)
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Formula("(SELECT COUNT(*) FROM Comments c WHERE c.post_id = id)")
    private int commentsCount;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    @Formula("(SELECT COUNT(*) FROM Likes l WHERE l.post_id = id)")
    private int likesCount;

    @Builder
    public Post(String content, Member author, String country) {
        this.content = content;
        this.author = author;
        this.country = country;
    }

    public void updateEntity(String content) {
        this.content = content;
    }

}
