package com.ssafy.ssafit.user.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class SignUpFailureException extends BusinessException {

    public SignUpFailureException(String message) {
        super(ErrorCode.SIGN_UP_FAILURE, message);
    }

    public static SignUpFailureException of(String message) {
        return new SignUpFailureException(message);
    }

}
