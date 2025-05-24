package com.ssafy.ssafit.video.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class VideoAccessUnauthorizedException extends BusinessException {

    private VideoAccessUnauthorizedException(String message) {
        super(ErrorCode.VIDEO_UNAUTHORIZED);
    }

    public static VideoAccessUnauthorizedException of(Long videoUserId, Long userId) {
        return new VideoAccessUnauthorizedException("비디오 작성자 회원Id: " + videoUserId + " 수정 요청 회원Id: " + userId);
    }
}
