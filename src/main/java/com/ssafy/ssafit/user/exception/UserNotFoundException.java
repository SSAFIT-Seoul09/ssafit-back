package com.ssafy.ssafit.user.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    private UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static UserNotFoundException of(ErrorCode errorCode) {
        return new UserNotFoundException(errorCode);
    }
}
