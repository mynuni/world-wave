package com.my.worldwave.api.youtube.client;

import com.my.worldwave.api.youtube.dto.VideoResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class YoutubeClient {
    private static final String BASE_URL = "https://www.googleapis.com";
    private static final String BASE_PATH = "/youtube/v3/videos";
    private static final int NUMBER_OF_VIDEOS_RETURN = 3;

    private final RestTemplate restTemplate;
    private final String API_KEY;

    public YoutubeClient(@Value("${api.youtube.key}") String apiKey, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.API_KEY = apiKey;
    }

    public VideoResponseDto getPopularVideos(String regionCode, String pageToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(BASE_URL + BASE_PATH)
                .queryParam("part", "snippet,statistics")
                .queryParam("chart", "mostPopular")
                .queryParam("regionCode", regionCode)
                .queryParam("maxResults", NUMBER_OF_VIDEOS_RETURN)
                .queryParam("key", API_KEY)
                .queryParam("pageToken", pageToken);

        return restTemplate.getForObject(builder.toUriString(), VideoResponseDto.class);

    }

}
