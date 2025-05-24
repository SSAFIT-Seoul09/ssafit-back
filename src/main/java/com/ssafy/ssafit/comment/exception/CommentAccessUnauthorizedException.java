package com.ssafy.ssafit.comment.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class CommentAccessUnauthorizedException extends BusinessException {

    private CommentAccessUnauthorizedException(String message) {
        super(ErrorCode.COMMENT_UNAUTHORIZED, message);
    }

    public static CommentAccessUnauthorizedException of(Long commentId) {
        return new CommentAccessUnauthorizedException("댓글Id : " + commentId + "에 대한 권한이 없습니다.");
    }
}
