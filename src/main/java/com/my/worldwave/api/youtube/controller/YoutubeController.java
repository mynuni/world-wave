package com.my.worldwave.api.youtube.controller;

import com.my.worldwave.api.youtube.client.YoutubeClient;
import com.my.worldwave.api.youtube.dto.VideoResponseDto;
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
public class YoutubeController {

    private final YoutubeClient youtubeClient;

    @GetMapping("/explore/popular/videos")
    @Cacheable(cacheNames = "youtubeCache")
    public Mono<VideoResponseDto> getPopularVideos(String regionCode, String pageToken) {
        return youtubeClient.getPopularVideos(regionCode, pageToken);
    }

}
