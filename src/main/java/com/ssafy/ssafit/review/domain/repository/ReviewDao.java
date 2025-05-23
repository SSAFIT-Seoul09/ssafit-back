package com.ssafy.ssafit.review.domain.repository;

import com.ssafy.ssafit.review.domain.model.Review;
import com.ssafy.ssafit.review.dto.response.ReviewResponseDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewDao {

    int insertReview(Review review);

    Review getReviewById(Long id);

    List<ReviewResponseDto> getAllReviews();

    List<ReviewResponseDto> getReviewByVideoId(@Param("videoId")Long videoId);

    List<ReviewResponseDto> getReviewByVideoIdAndReviewId(@Param("videoId") Long videoId, @Param("reviewId") Long reviewId);

    int updateReview(Review updateReview);

    int deleteById(@Param("id") Long id);

    void increaseViewCnt(@Param("reviewId") Long reviewId);
}
