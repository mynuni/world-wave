package com.my.worldwave.api.youtube.controller;

import com.my.worldwave.api.youtube.client.YoutubeClient;
import com.my.worldwave.api.youtube.model.Video;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/popular-videos")
    public Mono<List<Video>> getPopularVideos(String regionCode) {
        log.info("국가 코드:{}", regionCode);
        return youtubeClient.getPopularVideos(regionCode);
    }

}
