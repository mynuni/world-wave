package com.my.worldwave.api.youtube.client;

import com.my.worldwave.api.youtube.dto.VideoResponseDto;
import com.my.worldwave.api.youtube.model.Video;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class YoutubeClient {
    private final String BASE_URL = "https://www.googleapis.com";
    private final String BASE_PATH = "/youtube/v3/videos";
    private final WebClient webClient;
    private final String API_KEY;
    private static final int NUMBER_OF_VIDEOS_RETURN = 4;

    public YoutubeClient(@Value("${youtube.api.key}") String apiKey) {
        this.API_KEY = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public Mono<List<Video>> getPopularVideos(String regionCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .queryParam("part", "snippet")
                        .queryParam("chart", "mostPopular")
                        .queryParam("regionCode", regionCode)
                        .queryParam("maxResults", NUMBER_OF_VIDEOS_RETURN)
                        .queryParam("key", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(VideoResponseDto.class)
                .map(response -> response.getItems());
    }

}
