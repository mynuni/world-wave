package com.my.worldwave.util;

import com.my.worldwave.util.dto.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadService {

    private final String uploadDirectory;
    private final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    public FileUploadService(@Value("${upload.path}") String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    public FileUploadResponse uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일을 선택해주세요.");
        }

        try {
            String originalFileName = file.getOriginalFilename();
            String uploadFileExtension = extractExtension(originalFileName);
            if (!isImageFile(uploadFileExtension)) {
                throw new IllegalArgumentException("jpg, jpeg, png 파일만 업로드 가능합니다.");
            }

            String storedFileName = UUID.randomUUID() + "_" + originalFileName;
            String storedFilePath = Paths.get(uploadDirectory, storedFileName).toString();
            Path path = Paths.get(storedFilePath).toAbsolutePath();
            file.transferTo(path.toFile());

            return FileUploadResponse.builder()
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .storedFilePath(storedFilePath)
                    .extension(uploadFileExtension)
                    .fileSize(file.getSize())
                    .build();
        } catch (IOException e) {
            throw new IOException("파일 업로드에 실패했습니다.", e);
        }

    }

    public void deleteFile(String storedFileName) throws IOException {
        String storedFilePath = Paths.get(uploadDirectory, storedFileName).toString();
        Path path = Paths.get(storedFilePath).toAbsolutePath();
        File file = path.toFile();
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                throw new IOException("파일 삭제에 실패했습니다.");
            } else {
                log.info("삭제된 파일:{}", storedFilePath);
            }
        }
    }

    private String extractExtension(String fileName) {
        int extensionDotIndex = fileName.lastIndexOf('.');
        if (extensionDotIndex == -1) {
            throw new IllegalArgumentException("올바르지 않은 형식의 파일입니다.");
        }
        return fileName.substring(extensionDotIndex + 1).toLowerCase();
    }

    private boolean isImageFile(String extension) {
        return ALLOWED_IMAGE_EXTENSIONS.contains(extension);
    }

}