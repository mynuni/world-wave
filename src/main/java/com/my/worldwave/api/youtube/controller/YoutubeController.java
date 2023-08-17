package com.my.worldwave.api.youtube.controller;

import com.my.worldwave.api.youtube.client.YoutubeClient;
import com.my.worldwave.api.youtube.model.Video;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class YoutubeController {

    private final YoutubeClient youtubeClient;
    private final CacheManager cacheManager;

    @GetMapping("/popular-videos")
    @Cacheable(cacheNames = "youtubeCache")
    public Mono<List<Video>> getPopularVideos(String regionCode) {
        Cache newsCache = cacheManager.getCache("newsCache");

        log.info("youtubeCache is null? {}", newsCache == null);
        if (newsCache != null) {
            Cache.ValueWrapper valueWrapper = newsCache.get(regionCode);
            log.info("valueWrapper is null? {}", valueWrapper == null);
        }
        return youtubeClient.getPopularVideos(regionCode);
    }

}
