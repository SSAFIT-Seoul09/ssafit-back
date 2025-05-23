package com.ssafy.ssafit.video.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class VideoUpdatedException extends BusinessException {

    private VideoUpdatedException(String message) {
        super(ErrorCode.VIDEO_UPDATE_FAILURE, message);
    }

    public static VideoUpdatedException of(Long videoId) {
        return new VideoUpdatedException("영상Id : " + videoId + "삭제에 실패하였습니다");
    }
}
