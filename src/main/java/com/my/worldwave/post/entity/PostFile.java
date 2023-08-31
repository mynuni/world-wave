package com.my.worldwave.post.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalFileName;
    private String filePath;
    private long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostFile(String originalFileName, String filePath, long fileSize, Post post) {
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.post = post;
    }

}
