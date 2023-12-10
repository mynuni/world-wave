package com.my.worldwave.api.youtube.dto;

import com.my.worldwave.api.youtube.model.PageInfo;
import com.my.worldwave.api.youtube.model.Video;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VideoResponseDto {
    private List<Video> items;
    private String nextPageToken;
    private String prevPageToken;
    private PageInfo pageInfo;
}
