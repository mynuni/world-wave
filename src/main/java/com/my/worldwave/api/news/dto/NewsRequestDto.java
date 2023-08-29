package com.my.worldwave.api.news.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class NewsRequestDto {
    private String country;
    private int pageSize;
    private int page;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsRequestDto that = (NewsRequestDto) o;
        return pageSize == that.pageSize && page == that.page && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, pageSize);
    }

}