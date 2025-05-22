package com.ssafy.ssafit.favorite.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class FavoriteException extends BusinessException {

    private FavoriteException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static FavoriteException of(ErrorCode errorCode) {
        return new FavoriteException(errorCode);
    }
}
