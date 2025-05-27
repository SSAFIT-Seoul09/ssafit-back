package com.ssafy.ssafit.review.service;

import com.ssafy.ssafit.review.domain.model.Review;
import com.ssafy.ssafit.review.domain.repository.ReviewDao;
import com.ssafy.ssafit.review.dto.request.ReviewRequestDto;
import com.ssafy.ssafit.review.dto.response.ReviewResponseDto;
import com.ssafy.ssafit.review.exception.*;
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

        int isInserted = reviewDao.insertReview(review);
        if(isInserted <= 0) {
            log.info("리뷰 등록 실패 : {} {} {}",userId, videoId, requestDto);
            throw ReviewInsertException.of(userId);
        }

        ReviewResponseDto reviewResponseDto = reviewDao.getReviewResponseDtoByReviewId(review.getId());

        if (reviewResponseDto == null) {
            throw ReviewNotFoundException.of(review.getId());
        }
        log.info("리뷰 작성 성공: {}", reviewResponseDto.getId());
        return reviewResponseDto;
    }

    @Override
    public List<ReviewResponseDto> getAllReviews() {
        log.info("모든 리뷰 조회 요청");

        List<ReviewResponseDto> list = reviewDao.getAllReviewResponseDto();
        log.info("리뷰 목록 조회 결과: {}개", list.size());

        return list;
    }

    @Transactional
    @Override
    public List<ReviewResponseDto> getReview(Long videoId, Long reviewId) {
        log.info("특정 리뷰 조회 요청: videoId={}, reviewId={}", videoId, reviewId);

        List<ReviewResponseDto> list = getReviewsByCondition(videoId, reviewId);

        log.info("리뷰 조회 성공: videoId={}, reviewId={}, 찾은 개수 : {}", videoId, reviewId, list.size());
        return list;
    }

    @Transactional
    @Override
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto requestDto) {
        log.info("리뷰 수정 요청: userId={}, reviewId={}, requestDto={}", userId, reviewId, requestDto);

        Review review = getReview(reviewId);

        isWrittenByUserId(userId, reviewId, review);

        // 객체 업데이트
        review.update(requestDto);

        // 디비 업데이트
        int isUpdated = reviewDao.updateReview(review);
        if (isUpdated <= 0) {
            log.error("리뷰 수정 실패: reviewId={}", reviewId);
            throw ReviewUpdateException.of(reviewId);
        }

        ReviewResponseDto reponseDto = reviewDao.getReviewResponseDtoByReviewId(reviewId);
        if (reponseDto == null) {
            log.info("리뷰 수정 실패: reviewId={}", reviewId);
            throw ReviewNotFoundException.of(reviewId);
        }

        log.info("리뷰 수정 완료: reviewId={}", reponseDto.getId());
        return reponseDto;
    }

    @Transactional
    @Override
    public void deleteReview(Long userId, Long reviewId) {
        log.info("리뷰 삭제 요청: userId={}, reviewId={}", userId, reviewId);

        Review review = getReview(reviewId);

        isWrittenByUserId(userId, reviewId, review);

        int isDeleted = reviewDao.deleteById(reviewId);
        if (isDeleted <= 0) {
            log.error("리뷰 삭제 실패: reviewId={}", reviewId);
            throw ReviewDeleteException.of(reviewId);
        }
        log.info("리뷰 삭제 완료: reviewId={}", reviewId);
    }

    @Override
    public List<ReviewResponseDto> getMyReviewList(Long userId) {
        List<ReviewResponseDto> list = reviewDao.getReviewResponseDtoByUserId(userId);
        log.info("회원이 작성한 리뷰 개수 : {} ", list.size());
        return list;
    }

    private List<ReviewResponseDto> getReviewsByCondition(Long videoId, Long reviewId) {
        if (reviewId == null) {
            return reviewDao.getReviewResponseDtoByVideoId(videoId);
        } else {
            reviewDao.increaseViewCnt(reviewId);
            return reviewDao.getReviewResponseDtoByVideoIdAndReviewId(videoId, reviewId);
        }
    }

    // 리뷰 존재 여부 확인
    private Review getReview(Long reviewId) {
        Review review = reviewDao.getReviewById(reviewId);
        if (review == null) {
            log.error("리뷰 없음: reviewId={}", reviewId);
            throw ReviewNotFoundException.of(reviewId);
        }
        return review;
    }

    // 요청자가 작성한 글인지 확인
    private void isWrittenByUserId(Long userId, Long reviewId, Review review) {
        if (!Objects.equals(review.getUserId(), userId)) {
            log.error("수정 권한 없음: userId={}는 reviewId={}의 수정 권한이 없습니다.", userId, reviewId);
            throw ReviewAccessUnauthorizedException.of(reviewId);
        }
    }
}
