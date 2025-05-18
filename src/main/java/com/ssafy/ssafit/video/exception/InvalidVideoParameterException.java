package com.ssafy.ssafit.video.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class InvalidVideoParameterException extends BusinessException {

    private InvalidVideoParameterException(String message) {
        super(ErrorCode.INVALID_INPUT_VALUE, message);
    }

    public static InvalidVideoParameterException ofPart(String part) {
        return new InvalidVideoParameterException("유효하지 않은 영상 파트입니다: " + part);
    }
}
