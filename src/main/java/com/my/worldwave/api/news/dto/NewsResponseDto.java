package com.my.worldwave.api.news.dto;

import com.my.worldwave.api.news.model.News;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsResponseDto {
    private String status;
    private int totalResults;
    private int page;
    private News[] articles;
}
