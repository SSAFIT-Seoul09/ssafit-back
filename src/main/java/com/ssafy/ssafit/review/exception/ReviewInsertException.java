package com.ssafy.ssafit.review.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class ReviewInsertException extends BusinessException {

    private ReviewInsertException(String message) {
        super(ErrorCode.Review_INSERT_FAILURE, message);
    }

    public static ReviewInsertException of(Long userId) {
        return new ReviewInsertException("리뷰 등록에 실패하였습니다. 사용자 Id : " + userId);
    }
}
