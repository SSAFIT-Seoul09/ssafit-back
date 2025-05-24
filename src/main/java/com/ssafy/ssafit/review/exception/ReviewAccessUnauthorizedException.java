package com.ssafy.ssafit.review.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class ReviewAccessUnauthorizedException extends BusinessException {

    private ReviewAccessUnauthorizedException(String message) {
        super(ErrorCode.REVIEW_UNAUTHORIZED, message);
    }

    public static ReviewAccessUnauthorizedException of (Long ReviewId) {
        return new ReviewAccessUnauthorizedException("리뷰Id : " + ReviewId + "에 수정권한이 없습니다.");
    }
}
