package com.my.worldwave.member.dto.request;

import com.my.worldwave.member.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
public class MemberSearchParam {
    private String country;
    private String nickname;
    private Gender gender;
    private Integer ageRange;
    private boolean hideFollowers = false;
    private int page;
    private final int size = 9;
    private final Sort.Direction direction = Sort.Direction.DESC;
    private String sort = "id";

    public Pageable toPageable() {
        return PageRequest.of(page, size, Sort.by(direction, sort));
    }

}
