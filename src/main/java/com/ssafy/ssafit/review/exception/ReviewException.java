package com.ssafy.ssafit.review.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class ReviewException extends BusinessException {

    private ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static ReviewException of (ErrorCode errorCode) {
        return new ReviewException(errorCode);
    }

}
