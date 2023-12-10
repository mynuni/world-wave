package com.my.worldwave.api.news.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class News {
    private String title;
    private String description;
    private String url;
    private String urlToImage;
}
