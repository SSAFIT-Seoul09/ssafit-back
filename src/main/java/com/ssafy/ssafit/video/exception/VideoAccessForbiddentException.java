package com.ssafy.ssafit.video.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class VideoAccessForbiddentException extends BusinessException {

    private VideoAccessForbiddentException(String message) {
        super(ErrorCode.VIDEO_UNAUTHORIZED);
    }
}
