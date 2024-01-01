package com.my.worldwave.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequest {
    private String content;
    private List<MultipartFile> files;

}
