package com.my.worldwave.api.news.dto;

import com.my.worldwave.api.news.model.News;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsResponseDto {
    private String status;
    private int totalResults;
    private News[] articles;
}
