package com.ssafy.ssafit.user.exception;

import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;

public class InvalidUserCredentialException extends BusinessException {

    private InvalidUserCredentialException(String message) {
        super(ErrorCode.INVALID_CREDENTIAL, message);
    }

    public static InvalidUserCredentialException ofEmail(String email) {
        return new InvalidUserCredentialException("이메일을 잘못 입력하였습니다 : " + email);
    }

    public static InvalidUserCredentialException ofPassword() {
        return new InvalidUserCredentialException("비밀번호를 잘못 입력하였습니다.");
    }
}
