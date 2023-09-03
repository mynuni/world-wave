package com.my.worldwave.util.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileUploadResponse {
    private String originalFileName;
    private String storedFileName;
    private String storedFilePath;
    private String extension;
    private long fileSize;

    @Builder
    public FileUploadResponse(String originalFileName, String storedFileName, String storedFilePath, String extension, long fileSize) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storedFilePath = storedFilePath;
        this.extension = extension;
        this.fileSize = fileSize;
    }

}
