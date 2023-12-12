package com.my.worldwave.api.news.client;

import com.my.worldwave.api.news.dto.NewsRequestDto;
import com.my.worldwave.api.news.dto.NewsResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public class NewsApiClient {
    private static final String BASE_URL = "https://newsapi.org";
    private static final String BASE_PATH = "/v2/top-headlines";
    private static final int NUMBER_OF_NEWS_RETURN = 3;

    private final RestTemplate restTemplate;
    private final String apiKey;

    public NewsApiClient(@Value("${api.news.key}") String apiKey, RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "newsCache", cacheManager = "cacheManager", key = "{#newsRequestDto.country, #newsRequestDto.page}", condition = "#newsRequestDto.page == 1")
    public NewsResponseDto getNews(NewsRequestDto newsRequestDto) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(BASE_URL + BASE_PATH)
                .queryParam("country", newsRequestDto.getCountry())
                .queryParam("pageSize", NUMBER_OF_NEWS_RETURN)
                .queryParam("page", newsRequestDto.getPage())
                .queryParam("apiKey", apiKey);

        return restTemplate.getForObject(builder.toUriString(), NewsResponseDto.class);
    }

}