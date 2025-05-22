package com.ssafy.ssafit.comment.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class CommentException extends BusinessException {

    private CommentException (ErrorCode errorCode) {
        super(errorCode);
    }

    public static CommentException of(ErrorCode errorCode) {
        return new CommentException(errorCode);
    }
}
