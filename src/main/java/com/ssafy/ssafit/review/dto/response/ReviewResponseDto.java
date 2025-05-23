package com.ssafy.ssafit.review.dto.response;

import com.ssafy.ssafit.review.domain.model.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReviewResponseDto {
    private Long reviewId;
    private Long userId;
    private Long videoId;
    private String title;
    private String content;
    private int rating;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ReviewResponseDto toDto(Review insertedReview) {
        return ReviewResponseDto.builder()
                .reviewId(insertedReview.getId())
                .userId(insertedReview.getUserId())
                .videoId(insertedReview.getVideoId())
                .title(insertedReview.getTitle())
                .content(insertedReview.getContent())
                .rating(insertedReview.getRating())
                .views(insertedReview.getViews())
                .createdAt(insertedReview.getCreatedAt())
                .modifiedAt(insertedReview.getModifiedAt())
                .build();
    }
}
