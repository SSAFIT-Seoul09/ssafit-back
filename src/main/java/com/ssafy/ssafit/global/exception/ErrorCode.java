package com.ssafy.ssafit.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /**
     * 에러 코드 규칙
     * - 4xxx : 클라이언트 에러
     * <p>
     * - 5xxx : 서버 에러
     * - 51xx : 일반적인 서버 에러
     * - 52xx : 데이터베이스 관련 에러
     */

    // 4000번대: 클라이언트 요청 오류
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, 4001, "잘못된 입력입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, 4002, "잘못된 타입의 값입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, 4003, "이미 존재하는 이메일입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, 4007, "이메일을 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, 4016, "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 4004, "존재하지 않는 사용자입니다."),
    USER_DELETE_FAILURE(HttpStatus.BAD_REQUEST, 4015, "회원 탈퇴에 실패했습니다."),
    SIGN_UP_FAILURE(HttpStatus.BAD_REQUEST, 4005, "회원가입에 실패했습니다."),
    USER_UPDATE_FAILURE(HttpStatus.BAD_REQUEST, 4006, "회원정보 수정에 실패했습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, 4007, "토큰을 찾을 수 없습니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, 4009, "유효하지 않는 JWT 서명입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 40010, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, 4011, "지원되지 않는 JWT 토큰입니다."),
    INVALID_JWT_CLAIMS(HttpStatus.BAD_REQUEST, 4012, "잘못된 JWT 토큰입니다."),
    BLACKLISTED_TOKEN(HttpStatus.BAD_REQUEST, 4013, "블랙리스트된 토큰입니다."),

    // 4010번대: 리소스 관련 오류
    VIDEO_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 4000, "해당 영상에 대한 권한이 없습니다."),
    VIDEO_NOT_FOUND(HttpStatus.NOT_FOUND, 4014, "해당 영상을 찾을 수 없습니다."),
    VIDEO_INSERT_FAILURE(HttpStatus.BAD_REQUEST, 4701, "영상 등록에 실패했습니다."),
    VIDEO_UPDATE_FAILURE(HttpStatus.BAD_REQUEST, 4702, "영상 수정에 실패했습니다."),
    VIDEO_DELETE_FAILURE(HttpStatus.BAD_REQUEST, 4703, "영상 삭제에 실패했습니다."),

    REVIEW_UNAUTHORIZED(HttpStatus.FORBIDDEN, 4015, "해당 리뷰에 대한 권한이 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, 4014, "해당 리뷰를 찾을 수 없습니다."),
    Review_INSERT_FAILURE(HttpStatus.BAD_REQUEST, 4015, "리뷰 등록에 실패했습니다."),
    REVIEW_UPDATE_FAILURE(HttpStatus.BAD_REQUEST, 4016, "영상 업데이트에 실패했습니다."),
    REVIEW_DELETE_FAILURE(HttpStatus.BAD_REQUEST, 4017, "영상 삭제에 실패했습니다."),

    // 4500번대: 댓글 관련 오류
    COMMENT_UNAUTHORIZED(HttpStatus.FORBIDDEN, 4502, "해당 댓글에 대한 권한이 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, 4501, "해당 댓글을 찾을 수 없습니다."),
    COMMENT_INSERT_FAILURE(HttpStatus.BAD_REQUEST, 4701, "영상 등록에 실패했습니다."),
    COMMENT_UPDATE_FAILURE(HttpStatus.BAD_REQUEST, 4702, "영상 수정에 실패했습니다."),
    COMMENT_DELETE_FAILURE(HttpStatus.BAD_REQUEST, 4703, "영상 삭제에 실패했습니다."),

    // 찜 관련 에러 코드
    FAVORITE_VIDEO_NOT_FOUND(HttpStatus.NOT_FOUND, 4602, "찜한 영상을 찾을 수 없습니다."),
    // 5000번대: 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}