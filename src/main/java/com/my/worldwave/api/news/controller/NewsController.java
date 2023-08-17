package com.my.worldwave.api.news.controller;

import com.my.worldwave.api.news.client.NewsApiClient;
import com.my.worldwave.api.news.dto.NewsRequestDto;
import com.my.worldwave.api.news.model.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class NewsController {

    private final NewsApiClient newsApiClient;
    private final CacheManager cacheManager;

    @GetMapping("/news")
    @Cacheable(cacheNames = "newsCache")
    public Mono<News[]> getNews(NewsRequestDto newsRequestDto) {
        Cache newsCache = cacheManager.getCache("newsCache");

        log.info("newsCache is null? {}", newsCache == null);
        if (newsCache != null) {
            Cache.ValueWrapper valueWrapper = newsCache.get(newsRequestDto);
            log.info("valueWrapper is null? {}", valueWrapper == null);
        }

        return newsApiClient.getNews(newsRequestDto);
    }

}