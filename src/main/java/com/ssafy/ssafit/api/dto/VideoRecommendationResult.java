package com.ssafy.ssafit.api.dto;

import java.util.List;

public record VideoRecommendationResult(
        String category,  // "ARM" , "CHEST" etc
        List<YoutubeVideo> videos
) {}