package com.ssafy.ssafit.global.util.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class CookieException extends BusinessException {

    private CookieException (ErrorCode errorCode) {
        super(errorCode);
    }

    public static CookieException of(ErrorCode errorCode) {
        return new CookieException(errorCode);
    }
}
