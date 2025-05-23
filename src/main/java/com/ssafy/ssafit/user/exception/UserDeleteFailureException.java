package com.ssafy.ssafit.user.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class UserDeleteFailureException extends BusinessException {

    private UserDeleteFailureException(String message) {
        super(ErrorCode.USER_DELETE_FAILURE, message);
    }

    public static UserDeleteFailureException of(Long userId) {
        return new UserDeleteFailureException("회원ID" + userId +  "탈퇴에 실패하였습니다.");
    }

}
