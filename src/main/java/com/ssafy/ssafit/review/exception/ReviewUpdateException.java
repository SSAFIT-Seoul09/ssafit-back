package com.ssafy.ssafit.review.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class ReviewUpdateException extends BusinessException {

    private ReviewUpdateException (String message) {
        super(ErrorCode.REVIEW_UPDATE_FAILURE, message);
    }

    public static ReviewUpdateException of (Long ReviewId) {
        return new ReviewUpdateException("리뷰Id : " + ReviewId + "를 업데이트하는데 실패하였습니다.");
    }
}
