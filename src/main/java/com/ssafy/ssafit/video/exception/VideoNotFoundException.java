package com.ssafy.ssafit.video.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class VideoNotFoundException extends BusinessException {

    private VideoNotFoundException (ErrorCode errorCode) {
        super(errorCode);
    }

    public static VideoNotFoundException of(ErrorCode errorCode) {
        return new VideoNotFoundException(errorCode);
    }
}
