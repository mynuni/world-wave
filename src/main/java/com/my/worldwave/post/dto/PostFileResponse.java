package com.my.worldwave.post.dto;

import com.my.worldwave.post.entity.PostFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostFileResponse {
    private String attachedFilePath;

    public static List<PostFileResponse> convertToDtoList(List<PostFile> files) {
        return files.stream()
                .map(file -> new PostFileResponse(file.getFilePath()))
                .collect(Collectors.toList());
    }

}
