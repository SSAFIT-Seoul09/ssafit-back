package com.ssafy.ssafit.review.service;

import com.ssafy.ssafit.review.dto.request.ReviewRequestDto;
import com.ssafy.ssafit.review.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {
    ReviewResponseDto createReview(Long userId, Long videoId, ReviewRequestDto requestDto);

    List<ReviewResponseDto> getReview(Long videoId, Long reviewId);

    List<ReviewResponseDto> getAllReviews();

    ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto requestDto);

    void deleteReview(Long userId, Long reviewId);

    List<ReviewResponseDto> getMyReviewList(Long userId);
}
