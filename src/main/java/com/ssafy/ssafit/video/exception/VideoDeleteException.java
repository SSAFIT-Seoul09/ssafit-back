package com.ssafy.ssafit.video.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class VideoDeleteException extends BusinessException {
    private VideoDeleteException(String message) {
        super(ErrorCode.VIDEO_DELETE_FAILURE, message);
    }

    public static VideoDeleteException of(Long videoId) {
        return new VideoDeleteException("영상Id : " + videoId + "를 삭제하는데 실패하였습니다.");
    }

}
