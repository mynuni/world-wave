package com.my.worldwave.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedMembersRequest {
    private int page = 0;
    private int size = 4;
    private String country;

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }

}
