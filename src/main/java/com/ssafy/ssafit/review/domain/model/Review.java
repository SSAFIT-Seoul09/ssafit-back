package com.ssafy.ssafit.review.domain.model;

import com.ssafy.ssafit.global.entity.TimeStamped;
import com.ssafy.ssafit.review.dto.request.ReviewRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends TimeStamped {

    private Long id;
    private Long userId;
    private Long videoId;
    private String title;
    private String content;
    private int rating;
    private int views;

    public static Review of(Long userId, Long videoId, ReviewRequestDto requestDto) {
        return Review.builder()
                .userId(userId)
                .videoId(videoId)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .rating(requestDto.getRating())
                .build();
    }

    public Review update(ReviewRequestDto requestDto) {
        return Review.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .rating(requestDto.getRating())
                .build();
    }
}
