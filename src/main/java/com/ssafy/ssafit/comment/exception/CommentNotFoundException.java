package com.ssafy.ssafit.comment.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class CommentNotFoundException extends BusinessException {

    private CommentNotFoundException (String message) {
        super(ErrorCode.COMMENT_NOT_FOUND, message);
    }

    public static CommentNotFoundException of() {
        return new CommentNotFoundException("댓글을 찾을 수 없습니다.");
    }
}
