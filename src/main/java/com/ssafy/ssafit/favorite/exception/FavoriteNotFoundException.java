package com.ssafy.ssafit.favorite.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class FavoriteNotFoundException extends BusinessException {

    private FavoriteNotFoundException(String message) {
        super(ErrorCode.FAVORITE_VIDEO_NOT_FOUND,  message);
    }

    public static FavoriteNotFoundException of(Long userId) {
        return new FavoriteNotFoundException("사용자Id : " + userId + "의 찜이 존재하지 않습니다.");
    }
}
