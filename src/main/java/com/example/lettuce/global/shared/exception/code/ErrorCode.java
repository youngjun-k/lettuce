package com.example.lettuce.global.shared.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사 오류입니다."),

    INVALID_FITNESS_GOAL(HttpStatus.BAD_REQUEST, "유효하지 않은 운동 목표입니다."),

    MALFORMED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "JWT 형식이 올바르지 않습니다."),
    UNSUPPORTED_JWT_EXCEPTION(HttpStatus.BAD_REQUEST, "지원하지 않는 JWT 형식입니다."),
    ILLEGAL_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, "잘못된 인자입니다."),
    NON_EXPIRED_ACCOUNT(HttpStatus.BAD_REQUEST, "만료되지 않은 계정입니다."),

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "유효하지 않은 비밀번호입니다."),
    INVALID_REQUEST_BODY_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 요청 바디 형식입니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 역할입니다."),

    COMMON_INVALID_PARAM(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),

    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 요청입니다."),
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다."),

    // 403
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없는 요청입니다."),
    USER_DISABLED(HttpStatus.FORBIDDEN, "사용자가 비활성화되었습니다."),
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "계정이 잠겨있습니다."),
    ACCOUNT_EXPIRED(HttpStatus.FORBIDDEN, "계정이 만료되었습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, "이메일이 인증되지 않았습니다."),

    // 404
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리소스입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    NOT_REGISTERED_EMAIL(HttpStatus.NOT_FOUND, "등록되지 않은 이메일입니다."),

    // 409
    CONFLICT(HttpStatus.CONFLICT, "중복된 리소스입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    EMAIL_ALREADY_VERIFIED(HttpStatus.CONFLICT, "이메일이 이미 인증되었습니다."),

    // 415
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입입니다."),

    // 422
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "처리할 수 없는 엔티티입니다."),

    // 423
    LOCKED(HttpStatus.LOCKED, "요펑한 리소스가 잠겨있습니다."),

    // 428
    PRECONDITION_REQUIRED(HttpStatus.PRECONDITION_REQUIRED, "요청을 처리하기 위한 사전 조건이 필요합니다."),

    // 426
    UPGRADE_REQUIRED(HttpStatus.UPGRADE_REQUIRED, "API 버전 업그레이드가 필요합니다."),

    // 429
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "너무 많은 요청입니다."),
    TOO_MANY_ATTEMPTS(HttpStatus.TOO_MANY_REQUESTS, "비밀번호 입력 횟수가 초과되었습니다."),

    // 431
    REQUEST_HEADER_FIELDS_TOO_LARGE(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE, "요청 헤더 필드가 너무 큽니다."),

    // 451
    UNAVAILABLE_FOR_LEGAL_REASONS(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, "법적 이유로 사용할 수 없습니다."),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    OPENAI_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OpenAI 오류가 발생했습니다."),
    S3_UPLOADER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드 오류가 발생했습니다."),

    // 503
    AUTHENTICATION_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "인증 서비스를 사용할 수 없습니다."),
    IMAGE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 처리 오류가 발생했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
