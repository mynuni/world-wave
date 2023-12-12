package com.my.worldwave.member.entity;

import com.my.worldwave.util.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "profile_images")
@Entity
@NoArgsConstructor
public class ProfileImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public ProfileImage(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    @Builder
    public ProfileImage(String originalFileName, String storedFileName, String storedFilePath, String extension, long fileSize) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storedFilePath = storedFilePath;
        this.extension = extension;
        this.fileSize = fileSize;
    }

    public void markDeleted() {
        this.isDeleted = true;
    }

}
