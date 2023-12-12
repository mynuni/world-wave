package com.my.worldwave.post.entity;

import com.my.worldwave.util.domain.BaseEntity;
import com.my.worldwave.util.dto.FileUploadResult;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "post_files")
@Entity
public class PostFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_file_id")
    private Long id;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "stored_file_name")
    private String storedFileName;

    @Column(name = "stored_file_path")
    private String storedFilePath;

    private String extension;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostFile(String originalFileName, String storedFileName, String storedFilePath, String extension, long fileSize, Post post) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storedFilePath = storedFilePath;
        this.extension = extension;
        this.fileSize = fileSize;
        this.post = post;
        this.isDeleted = false;
    }

    public static PostFile of(FileUploadResult fileUploadResult, Post post) {
        return PostFile.builder()
                .originalFileName(fileUploadResult.getOriginalFileName())
                .storedFileName(fileUploadResult.getStoredFileName())
                .storedFilePath(fileUploadResult.getStoredFilePath())
                .extension(fileUploadResult.getExtension())
                .fileSize(fileUploadResult.getFileSize())
                .post(post)
                .build();
    }

    // 연관 관계 변경을 위한 setter 메서드
    public void setPost(Post post) {
        this.post = post;
    }

}
