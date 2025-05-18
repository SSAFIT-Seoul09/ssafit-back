package com.ssafy.ssafit.video.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class VideoNotFoundException extends BusinessException {

    private VideoNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }

    public static VideoNotFoundException ofId(Long id) {
        return new VideoNotFoundException("해당 ID의 영상이 존재하지 않습니다: " + id);
    }

    public static VideoNotFoundException ofTitle(String title) {
        return new VideoNotFoundException("해당 제목의 영상이 존재하지 않습니다: " + title);
    }
}
