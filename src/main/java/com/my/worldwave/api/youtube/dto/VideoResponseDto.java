package com.my.worldwave.api.youtube.dto;

import com.my.worldwave.api.youtube.model.Video;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VideoResponseDto {
    private List<Video> items;
}
