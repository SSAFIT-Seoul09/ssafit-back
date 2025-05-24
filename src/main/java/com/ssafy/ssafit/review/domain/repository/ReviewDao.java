package com.ssafy.ssafit.review.domain.repository;

import com.ssafy.ssafit.review.domain.model.Review;
import com.ssafy.ssafit.review.dto.response.ReviewResponseDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewDao {

    int insertReview(Review review);

    Review getReviewById(@Param("reviewId") Long reviewId);

    List<ReviewResponseDto> getReviewResponseDtoByVideoId(@Param("videoId")Long videoId);

    List<ReviewResponseDto> getReviewResponseDtoByVideoIdAndReviewId(@Param("videoId") Long videoId, @Param("reviewId") Long reviewId);

    int updateReview(Review review);

    int deleteById(@Param("id") Long id);

    void increaseViewCnt(@Param("reviewId") Long reviewId);

    ReviewResponseDto getReviewResponseDtoByReviewId(@Param("reviewId") Long reviewId);

    List<ReviewResponseDto> getAllReviewResponseDto();
}
