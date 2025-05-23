package com.ssafy.ssafit.comment.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class CommentDeleteException extends BusinessException {

    private CommentDeleteException (String message) {
        super(ErrorCode.COMMENT_DELETE_FAILURE, message);
    }

    public static CommentDeleteException of(Long commentId) {
        return new CommentDeleteException("댓글 Id : " + commentId + "삭제하는데 실패하였습니다.");
    }
}
