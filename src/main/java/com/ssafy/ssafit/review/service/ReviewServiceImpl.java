package com.ssafy.ssafit.review.service;

import com.ssafy.ssafit.review.domain.model.Review;
import com.ssafy.ssafit.review.domain.repository.ReviewDao;
import com.ssafy.ssafit.review.dto.request.ReviewRequestDto;
import com.ssafy.ssafit.review.dto.response.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j(topic = "ReviewServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;

    @Transactional
    @Override
    public ReviewResponseDto createReview(Long userId, Long videoId, ReviewRequestDto requestDto) {
        log.info("리뷰 작성 요청 : {} {} {}",userId, videoId, requestDto);
        Review review = Review.of(userId, videoId, requestDto);
        reviewDao.insertReview(review);
        log.debug("리뷰 작성 삽입 완료");

        Review insertedReview = reviewDao.getReviewById(review.getId());
        log.debug("리뷰 삽입 확인 완료");

        if (insertedReview == null) {
            throw new IllegalArgumentException("예외처리");
        }
        log.info("리뷰 작성 성공: {}", insertedReview.getId());
        return ReviewResponseDto.toDto(insertedReview);
    }

    @Override
    public List<ReviewResponseDto> getAllReviews() {
        List<ReviewResponseDto> list = reviewDao.getAllReviews();
        if (list.isEmpty()) {
            throw new IllegalArgumentException("리뷰가 존재하지 않습니다");
        }
        return list;
    }

    @Override
    public List<ReviewResponseDto> getReview(Long videoId, Long reviewId) {
        List<ReviewResponseDto> list = getReviews(videoId, reviewId);

        if (list.isEmpty()) {
            throw new IllegalArgumentException("리뷰가 존재하지 않습니다");
        }
        return list;
    }

    @Transactional
    @Override
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto requestDto) {
        Review review = reviewDao.getReviewById(reviewId);
        if (review == null) {
            throw new IllegalArgumentException("존재하지 않는 리뷰입니다");
        }

        // 본인 작성 리뷰인지 확인
        if (!Objects.equals(review.getUserId(), userId)) {
            throw new IllegalArgumentException("수정권한이 없습니다.");
        }

        // 객체 업데이트
        review.update(requestDto);

        // 디비 업데이트
        int isUpdated = reviewDao.updateReview(review);
        if (isUpdated <= 0) {
            throw new IllegalArgumentException("업데이트에 실패하였습니다");
        }

        Review insertedReview = reviewDao.getReviewById(reviewId);
        return ReviewResponseDto.toDto(insertedReview);
    }

    @Transactional
    @Override
    public void deleteReview(Long userId, Long reviewId) {
        // 존재 여부 확인
        Review review = reviewDao.getReviewById(reviewId);
        if (review == null) {
            throw new IllegalArgumentException("존재하지 않는 리뷰입니다.");
        }

        int isDeleted = reviewDao.deleteById(reviewId);
        if (isDeleted <= 0) {
            throw new IllegalArgumentException("리뷰 삭제에 실패하였습니다.");
        }
    }

    private List<ReviewResponseDto> getReviews(Long videoId, Long reviewId) {
        if (reviewId == null) {
            return reviewDao.getReviewByVideoId(videoId);
        } else {
            return reviewDao.getReviewByVideoIdAndReviewId(videoId, reviewId);
        }
    }


}
