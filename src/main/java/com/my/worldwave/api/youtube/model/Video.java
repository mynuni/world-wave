package com.my.worldwave.api.youtube.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class Video {
    private String id;
    private Snippet snippet;
    private Statistics statistics;

    @Getter
    @Setter
    public static class Snippet {
        private String channelTitle;
        private String title;
        private String description;
        private String publishedAt;
        private Thumbnail thumbnails;
    }

    @Getter
    @Setter
    public static class Thumbnail {
        private ThumbnailItem defaultThumbnail;
        private ThumbnailItem medium;
        private ThumbnailItem high;
    }

    @Getter
    @Setter
    public static class ThumbnailItem {
        private String url;
        private int width;
        private int height;
    }

    @Getter
    @Setter
    public static class Statistics {
        private Long viewCount;
    }

}
