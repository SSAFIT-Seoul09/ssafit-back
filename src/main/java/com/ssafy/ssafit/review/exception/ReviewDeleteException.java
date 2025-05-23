package com.ssafy.ssafit.review.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class ReviewDeleteException extends BusinessException {

    private ReviewDeleteException (String message) {
        super(ErrorCode.REVIEW_DELETE_FAILURE, message);
    }

    public static ReviewDeleteException of(Long ReviewId) {
        return new ReviewDeleteException("리뷰Id : " + ReviewId + "삭제에 실패하였습니다.");
    }
}
