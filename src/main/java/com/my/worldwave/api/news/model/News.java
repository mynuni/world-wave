package com.my.worldwave.api.news.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class News {
    private String title;
    private String description;
    private String url;
    private String urlToImage;
}
