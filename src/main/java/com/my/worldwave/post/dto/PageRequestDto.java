package com.my.worldwave.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
public class PageRequestDto {
    private int page = 0;
    private int size = 5;
    private Sort.Direction direction = Sort.Direction.DESC;
    private String sort;

//    public Pageable toPageable() {
//        return PageRequest.of(page, size, direction, sort);
//    }

    public Pageable toPageable() {
        if ("likes".equals(sort)) {
            return PageRequest.of(page, size, direction, "likesCount");
        } else if("comments".equals(sort)){
            return PageRequest.of(page, size, direction, "commentsCount");
        } else {
            return PageRequest.of(page, size, direction, "createdAt");
        }

    }
}