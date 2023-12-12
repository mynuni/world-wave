package com.my.worldwave.api.news.controller;

import com.my.worldwave.api.news.client.NewsApiClient;
import com.my.worldwave.api.news.dto.NewsRequestDto;
import com.my.worldwave.api.news.dto.NewsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class NewsController {

    private final NewsApiClient newsApiClient;

    @GetMapping("/explore/popular/news")
    public NewsResponseDto getNews(NewsRequestDto newsRequestDto) {
        return newsApiClient.getNews(newsRequestDto);
    }

}