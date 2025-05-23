package com.ssafy.ssafit.review.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class ReviewNotFoundException extends BusinessException {

    private ReviewNotFoundException (String message) {
        super(ErrorCode.REVIEW_NOT_FOUND, message);
    }

    public static ReviewNotFoundException of(Long ReviewId) {
        return new ReviewNotFoundException("리뷰를 찾을 수 없습니다. 리뷰Id : " + ReviewId);
    }

    public static ReviewNotFoundException of() {
        return new ReviewNotFoundException("리뷰를 찾을 수 없습니다.");
    }

}
