package com.ssafy.ssafit.global.util.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class TokenException extends BusinessException {

    private TokenException(ErrorCode errorCode){
        super(errorCode);
    }

    public static TokenException of(ErrorCode errorCode) {
        return new TokenException(errorCode);
    }


}
