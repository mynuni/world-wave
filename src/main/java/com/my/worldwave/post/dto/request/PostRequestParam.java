package com.my.worldwave.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestParam {
    private int page = 0;
    private final int size = 5;
    private final Sort.Direction direction = Sort.Direction.DESC;
    private String sort = "createdAt";
    private Long memberId;

    public Pageable toPageable() {
        switch (sort) {
            case "comment":
                sort = "commentCount";
                break;
            case "like":
                sort = "likeCount";
                break;
            default:
                sort = "createdAt";
        }

        return PageRequest.of(page, size, Sort.by(direction, sort));
    }

}
