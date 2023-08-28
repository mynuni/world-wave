package com.my.worldwave.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileUploadService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    public String uploadFile(MultipartFile file) {
        try {
            String storedFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, storedFileName).toString();
            Path path = Paths.get(filePath).toAbsolutePath();
            file.transferTo(path.toFile());
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}