package com.ssafy.ssafit.api.dto;

public record YoutubeVideo(
        String title,
        String url,
        String thumbnailUrl,
        int durationSeconds
) {}
