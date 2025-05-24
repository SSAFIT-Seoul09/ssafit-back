package com.ssafy.ssafit.review.controller;

import com.ssafy.ssafit.global.auth.AuthenticatedUser;
import com.ssafy.ssafit.global.auth.annotation.LoginUser;
import com.ssafy.ssafit.global.response.ApiResponse;
import com.ssafy.ssafit.review.dto.request.ReviewRequestDto;
import com.ssafy.ssafit.review.dto.response.ReviewResponseDto;
import com.ssafy.ssafit.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "ReviewController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰작성
    @PostMapping("/{videoId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(@LoginUser AuthenticatedUser authenticatedUser,
                                                                       @PathVariable(name = "videoId") Long videoId,
                                                                       @RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto responseDto = reviewService.createReview(authenticatedUser.getUserId(), videoId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("리뷰 작성이 완료되었습니다.", responseDto));
    }

    // 젠체 리뷰 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getAllReviews() {
        List<ReviewResponseDto> list = reviewService.getAllReviews();
        return ResponseEntity.ok(ApiResponse.success("리뷰 전체 목록을 조회하였습니다.", list));
    }

    // 특정 비디오의 리뷰 조회. reviewId 입력시 상세조회
    @GetMapping("/{videoId}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getReviews(@PathVariable(name = "videoId", required = false) Long videoId,
                                                                           @RequestParam(name = "reviewId", required = false) Long reviewId) {
        List<ReviewResponseDto> list = reviewService.getReview(videoId, reviewId);
        return ResponseEntity.ok(ApiResponse.success("리뷰를 불러왔습니다.", list));
    }

    // 리뷰 수정
    @PatchMapping("{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReview(@LoginUser AuthenticatedUser authenticatedUser,
                                                                       @PathVariable(name = "reviewId") Long reviewId,
                                                                       @RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto responseDto = reviewService.updateReview(authenticatedUser.getUserId(), reviewId, requestDto);
        return ResponseEntity.ok(ApiResponse.success("리뷰를 업데이트하였습니다.", responseDto));
    }

    // 리뷰 삭제
    @DeleteMapping("{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@LoginUser AuthenticatedUser authenticatedUser,
                                                         @PathVariable(name = "reviewId") Long reviewId) {
        reviewService.deleteReview(authenticatedUser.getUserId(), reviewId);
        return ResponseEntity.ok(ApiResponse.success("리뷰 삭제에 성공하였습니다."));
    }
}
