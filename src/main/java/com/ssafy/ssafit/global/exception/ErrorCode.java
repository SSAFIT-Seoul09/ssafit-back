package com.ssafy.ssafit.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /**
     * 에러 코드 규칙
     * - 4xxx : 클라이언트 에러
     *
     * - 5xxx : 서버 에러
     *  - 51xx : 일반적인 서버 에러
     *  - 52xx : 데이터베이스 관련 에러
     *
     */

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, 4001, "잘못된 입력입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, 4002, "잘못된 타입의 값입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, 4003, "이미 존재하는 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 4004, "존재하지 않는 사용자입니다."),
    INVALID_CREDENTIAL(HttpStatus.UNAUTHORIZED, 4005, "이메일 또는 비밀번호를 잘못 입력하였습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "서버 오류가 발생했습니다."),


    // GPT로 임의로 설정해놓은 에러코드,
    DUPLICATE_RESOURCE(HttpStatus.BAD_REQUEST, 4003, "이미 존재하는 리소스입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 4010, "인증이 필요합니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 4040, "리소스를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}