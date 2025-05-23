package com.ssafy.ssafit.comment.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class CommentUpdateException extends BusinessException {

    private CommentUpdateException (String message) {
        super(ErrorCode.COMMENT_UPDATE_FAILURE, message);
    }

    public static CommentUpdateException of(Long commentId) {
        return new CommentUpdateException("댓글 Id : " + commentId + " 수정을 실패하였습니다.");
    }
}
