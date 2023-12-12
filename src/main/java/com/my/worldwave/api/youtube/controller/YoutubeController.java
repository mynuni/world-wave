package com.my.worldwave.api.youtube.controller;

import com.my.worldwave.api.youtube.client.YoutubeClient;
import com.my.worldwave.api.youtube.dto.VideoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class YoutubeController {

    private final YoutubeClient youtubeClient;

    @GetMapping("/explore/popular/videos")
    public VideoResponseDto getPopularVideos(String regionCode, String pageToken) {
        return youtubeClient.getPopularVideos(regionCode, pageToken);
    }

}
