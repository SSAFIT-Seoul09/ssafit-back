package com.ssafy.ssafit.user.exception;


import com.ssafy.ssafit.global.exception.BusinessException;
import com.ssafy.ssafit.global.exception.ErrorCode;
import lombok.Getter;

/**
 * 이메일 중복 시 발생
 */
@Getter
public class EmailAlreadyExistException extends BusinessException {

    private EmailAlreadyExistException(String message) {
        super(ErrorCode.EMAIL_ALREADY_EXISTS, message);
    }

    public static EmailAlreadyExistException of(String email) {
        return new EmailAlreadyExistException("이미 존재하는 이메일입니다 : " + email);
    }
}


