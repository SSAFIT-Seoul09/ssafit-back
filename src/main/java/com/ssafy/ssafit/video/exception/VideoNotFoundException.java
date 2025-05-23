package com.ssafy.ssafit.video.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class VideoNotFoundException extends BusinessException {

    private VideoNotFoundException (ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static VideoNotFoundException of(Long videoId) {
        return new VideoNotFoundException(ErrorCode.VIDEO_NOT_FOUND, "영상ID : " + videoId + "를 찾을 수 없습니다.");
    }

}
