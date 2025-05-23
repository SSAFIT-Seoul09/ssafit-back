package com.ssafy.ssafit.comment.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class CommentAccessForbiddenException extends BusinessException {

    private CommentAccessForbiddenException (String message) {
        super(ErrorCode.COMMENT_UNAUTHORIZED, message);
    }

    public static CommentAccessForbiddenException of(Long commentId) {
        return new CommentAccessForbiddenException("댓글Id : " + commentId + "에 대한 권한이 없습니다.");
    }
}
