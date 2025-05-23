package com.ssafy.ssafit.comment.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class CommentInsertException extends BusinessException {

    private CommentInsertException (String message) {
        super(ErrorCode.COMMENT_INSERT_FAILURE, message);
    }

    public static CommentInsertException of(Long userId) {
        return new CommentInsertException("댓글 등록에 실패하였습니다. 사용자Id " + userId);
    }
}
