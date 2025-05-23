package com.ssafy.ssafit.user.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    private UserNotFoundException(String messages) {
        super(ErrorCode.TOKEN_NOT_FOUND, messages);
    }

    public static UserNotFoundException of(String messages) {
        return new UserNotFoundException(messages);
    }

    public static UserNotFoundException ofUserId(Long userId) {
        return new UserNotFoundException("회원 ID" + userId + "를 찾을 수 없습니다.");
    }
}
