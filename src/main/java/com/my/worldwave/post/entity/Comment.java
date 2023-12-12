package com.my.worldwave.post.entity;

import com.my.worldwave.member.entity.Member;
import com.my.worldwave.util.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "comments")
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(String content, Member author, Post post) {
        this.content = content;
        this.post = post;
        this.author = author;
    }

    public void updateContent(String content) {
        this.content = content;
    }

}
