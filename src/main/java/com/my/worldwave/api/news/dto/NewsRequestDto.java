package com.my.worldwave.api.news.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class NewsRequestDto {
    private String country;
    private int pageSize;
    private int page;

}