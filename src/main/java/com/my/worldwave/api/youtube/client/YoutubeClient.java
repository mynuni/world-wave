package com.my.worldwave.api.youtube.client;

import com.my.worldwave.api.youtube.dto.VideoResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class YoutubeClient {
    private static final String BASE_URL = "https://www.googleapis.com";
    private static final String BASE_PATH = "/youtube/v3/videos";
    private static final int NUMBER_OF_VIDEOS_RETURN = 3;
    private final WebClient webClient;
    private final String API_KEY;

    public YoutubeClient(@Value("${api.youtube.key}") String apiKey) {
        this.API_KEY = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public Mono<VideoResponseDto> getPopularVideos(String regionCode, String pageToken) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .queryParam("part", "snippet,statistics")
                        .queryParam("chart", "mostPopular")
                        .queryParam("regionCode", regionCode)
                        .queryParam("maxResults", NUMBER_OF_VIDEOS_RETURN)
                        .queryParam("key", API_KEY)
                        .queryParam("pageToken", pageToken)
                        .build())
                .retrieve()
                .bodyToMono(VideoResponseDto.class);
    }

}
