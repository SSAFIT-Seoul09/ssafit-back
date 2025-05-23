package com.ssafy.ssafit.review.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class ReviewAccessUnauthorized extends BusinessException {

    private ReviewAccessUnauthorized (String message) {
        super(ErrorCode.REVIEW_UNAUTHORIZED, message);
    }

    public static ReviewAccessUnauthorized of (Long ReviewId) {
        return new ReviewAccessUnauthorized("리뷰Id : " + ReviewId + "에 수정권한이 없습니다.");
    }
}
