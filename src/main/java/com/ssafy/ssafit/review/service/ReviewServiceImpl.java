package com.ssafy.ssafit.review.service;

import com.ssafy.ssafit.review.domain.model.Review;
import com.ssafy.ssafit.review.domain.repository.ReviewDao;
import com.ssafy.ssafit.review.dto.request.ReviewRequestDto;
import com.ssafy.ssafit.review.dto.response.ReviewResponseDto;
import com.ssafy.ssafit.review.exception.*;
import com.ssafy.ssafit.user.domain.model.User;
import com.ssafy.ssafit.user.domain.repository.UserDao;
import com.ssafy.ssafit.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j(topic = "ReviewServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;
    private final UserDao userDao;

    @Transactional
    @Override
    public ReviewResponseDto createReview(Long userId, Long videoId, ReviewRequestDto requestDto) {
        log.info("리뷰 작성 요청 : {} {} {}",userId, videoId, requestDto);

        isUserValid(userId);

        Review review = Review.of(userId, videoId, requestDto);

        int isInserted = reviewDao.insertReview(review);
        if(isInserted <= 0) {
            log.info("리뷰 등록 실패 : {} {} {}",userId, videoId, requestDto);
            throw ReviewInsertException.of(userId);
        }

        Review insertedReview = reviewDao.getReviewById(review.getId());

        if (insertedReview == null) {
            throw ReviewNotFoundException.of(insertedReview.getId());
        }
        log.info("리뷰 작성 성공: {}", insertedReview.getId());
        return ReviewResponseDto.toDto(insertedReview);
    }

    @Override
    public List<ReviewResponseDto> getAllReviews() {
        log.info("모든 리뷰 조회 요청");

        List<ReviewResponseDto> list = reviewDao.getAllReviews();
        log.info("리뷰 목록 조회 결과: {}개", list.size());

        return list;
    }

    @Transactional
    @Override
    public List<ReviewResponseDto> getReview(Long videoId, Long reviewId) {
        log.info("특정 리뷰 조회 요청: videoId={}, reviewId={}", videoId, reviewId);

        List<ReviewResponseDto> list = getReviews(videoId, reviewId);

        log.info("리뷰 조회 성공: videoId={}, reviewId={}, 찾은 개수 : {}", videoId, reviewId, list.size());
        return list;
    }

    @Transactional
    @Override
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto requestDto) {
        log.info("리뷰 수정 요청: userId={}, reviewId={}, requestDto={}", userId, reviewId, requestDto);

        isUserValid(userId);

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

        Review insertedReview = reviewDao.getReviewById(reviewId);
        if (insertedReview == null) {
            log.info("리뷰 수정 실패: reviewId={}", insertedReview.getId());
            throw ReviewNotFoundException.of(insertedReview.getId());
        }
        log.info("리뷰 수정 완료: reviewId={}", insertedReview.getId());

        return ReviewResponseDto.toDto(insertedReview);
    }

    @Transactional
    @Override
    public void deleteReview(Long userId, Long reviewId) {
        log.info("리뷰 삭제 요청: userId={}, reviewId={}", userId, reviewId);

        isUserValid(userId);

        Review review = getReview(reviewId);

        isWrittenByUserId(userId, reviewId, review);

        int isDeleted = reviewDao.deleteById(reviewId);
        if (isDeleted <= 0) {
            log.error("리뷰 삭제 실패: reviewId={}", reviewId);
            throw ReviewDeleteException.of(reviewId);
        }
        log.info("리뷰 삭제 완료: reviewId={}", reviewId);
    }

    private List<ReviewResponseDto> getReviews(Long videoId, Long reviewId) {
        if (reviewId == null) {
            return reviewDao.getReviewByVideoId(videoId);
        } else {
            reviewDao.increaseViewCnt(reviewId);
            return reviewDao.getReviewByVideoIdAndReviewId(videoId, reviewId);
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

    // 존재하는 회원의 요청인지 확인
    private boolean isUserValid(Long userId) {
        User user = Optional.ofNullable(userDao.findUserById(userId))
                .orElseThrow(() -> {
                    log.error("해당 userId : {}는 존재하지 않는 회원입니다.", userId);
                    throw UserNotFoundException.ofUserId(userId);
                });
        return user != null;
    }

    // 요청자가 작성한 글인지 확인
    private void isWrittenByUserId(Long userId, Long reviewId, Review review) {
        if (!Objects.equals(review.getUserId(), userId)) {
            log.error("수정 권한 없음: userId={}는 reviewId={}의 수정 권한이 없습니다.", userId, reviewId);
            throw ReviewAccessUnauthorizedException.of(reviewId);
        }
    }
}
