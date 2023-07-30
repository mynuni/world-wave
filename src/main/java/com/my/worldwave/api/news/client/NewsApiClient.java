package com.my.worldwave.api.news.client;

import com.my.worldwave.api.news.dto.NewsRequestDto;
import com.my.worldwave.api.news.dto.NewsResponseDto;
import com.my.worldwave.api.news.model.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class NewsApiClient {
    private final String BASE_URL = "https://newsapi.org";
    private final String BASE_PATH = "/v2/top-headlines";
    private final WebClient webClient;
    private final String API_KEY;

    public NewsApiClient(@Value("${news.api.key}") String apiKey) {
        this.API_KEY = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public Mono<News[]> getNews(NewsRequestDto newsRequestDto) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .queryParam("country", newsRequestDto.getCountry())
                        .queryParam("pageSize", newsRequestDto.getPageSize())
                        .queryParam("apiKey", API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(NewsResponseDto.class)
                .map(response -> response.getArticles());
    }

}
