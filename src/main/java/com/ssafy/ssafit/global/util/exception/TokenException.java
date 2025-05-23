package com.ssafy.ssafit.global.util.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class TokenException extends BusinessException {

    private TokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static TokenException of(ErrorCode errorCode, String message) {
        return new TokenException(errorCode, message);
    }


}
