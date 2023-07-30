package com.my.worldwave.api.news.dto;

import com.my.worldwave.api.news.model.News;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class NewsRequestDto {
    private String country;
    private int pageSize;

}