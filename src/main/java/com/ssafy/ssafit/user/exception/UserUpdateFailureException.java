package com.ssafy.ssafit.user.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class UserUpdateFailureException extends BusinessException {

    private UserUpdateFailureException(String message) {
        super(ErrorCode.USER_UPDATE_FAILURE, message);
    }

    public static UserUpdateFailureException of(Long userId) {
        return new UserUpdateFailureException(userId + "의 사용자 정보 수정에 실패하였습니다");
    }

}
