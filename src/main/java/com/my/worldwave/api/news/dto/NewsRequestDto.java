package com.my.worldwave.api.news.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class NewsRequestDto {
    private String country;
    private int pageSize;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        NewsRequestDto newsRequestDto = (NewsRequestDto) obj;
        return Objects.equals(this.country, newsRequestDto.country) && this.pageSize == newsRequestDto.pageSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, pageSize);
    }

}