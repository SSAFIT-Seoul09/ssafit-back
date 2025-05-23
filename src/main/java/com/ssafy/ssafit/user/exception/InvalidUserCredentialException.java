package com.ssafy.ssafit.user.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class InvalidUserCredentialException extends BusinessException {

    private InvalidUserCredentialException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static InvalidUserCredentialException ofEmail(String email) {
        return new InvalidUserCredentialException(ErrorCode.EMAIL_NOT_FOUND, "이메일을 잘못 입력하였습니다 : " + email);
    }

    public static InvalidUserCredentialException ofPassword() {
        return new InvalidUserCredentialException(ErrorCode.INVALID_PASSWORD, "비밀번호를 잘못 입력하였습니다.");
    }
}
