package com.my.worldwave.post.entity;

import com.my.worldwave.util.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "POSTS")
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false, length = 20)
    private String author;

    @Column(nullable = false, length = 2)
    private String country;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(String title, String content, String author, String country) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.country = country;
    }

    public void updateEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
