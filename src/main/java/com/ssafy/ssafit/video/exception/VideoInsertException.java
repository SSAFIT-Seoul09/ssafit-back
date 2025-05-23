package com.ssafy.ssafit.video.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class VideoInsertException extends BusinessException {

    private VideoInsertException(String message) {
        super(ErrorCode.VIDEO_INSERT_FAILURE, message);
    }

    public static VideoInsertException of(String message) {
        return new VideoInsertException(message);
    }
}
