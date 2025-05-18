package com.ssafy.ssafit.user.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class SignUpFailureException extends BusinessException {

    private SignUpFailureException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static SignUpFailureException of(ErrorCode errorCode) {
        return new SignUpFailureException(errorCode);
    }

}
