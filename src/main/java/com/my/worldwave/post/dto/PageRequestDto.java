package com.my.worldwave.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class PageRequestDto {
    private int page = 0;
    private int size = 5;
    private Sort.Direction direction = Sort.Direction.DESC;
    private String sort = "id";

    public Pageable toPageable() {
        return PageRequest.of(page, size, direction, sort);
    }

}
