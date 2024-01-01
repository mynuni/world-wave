package com.my.worldwave.post.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchRequest {
    private int page = 0;
    private int size = 5;
    private Sort.Direction direction = Sort.Direction.DESC;
    private String sort = "createdAt";

    public Pageable toPageable() {
        return PageRequest.of(page, size, direction, sort);
    }

}
