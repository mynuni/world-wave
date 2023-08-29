package com.my.worldwave.api.youtube.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Video {
    private String id;
    private Snippet snippet;

    @Getter
    @Setter
    public static class Snippet {
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

}