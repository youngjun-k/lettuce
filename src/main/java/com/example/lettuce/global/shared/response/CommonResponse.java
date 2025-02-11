package com.example.lettuce.global.shared.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.lettuce.global.shared.exception.code.ErrorCode;
import com.example.lettuce.global.shared.exception.code.SuccessCode;

import java.io.Serializable;

public record CommonResponse<T>(
        boolean success,
        String message,
        T result) implements Serializable {

    public static <T> ResponseEntity<CommonResponse<VoidResponse>> success(SuccessCode successCode) {
        return ResponseEntity.status(successCode.getHttpStatus())
                .body(new CommonResponse<>(true, successCode.getMessage(), VoidResponse.getInstance()));
    }

    public static <T> ResponseEntity<CommonResponse<T>> success(SuccessCode successCode, T data) {
        return ResponseEntity.status(successCode.getHttpStatus())
                .body(new CommonResponse<>(true, successCode.getMessage(), data));
    }

    public static <T> ResponseEntity<CommonResponse<VoidResponse>> fail(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new CommonResponse<>(false, errorCode.getMessage(), VoidResponse.getInstance()));
    }

    public static <T> ResponseEntity<CommonResponse<T>> fail(ErrorCode errorCode, T data) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new CommonResponse<>(false, errorCode.getMessage(), data));
    }

    public static <T> ResponseEntity<CommonResponse<VoidResponse>> fail(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse<>(false, exception.getMessage(), VoidResponse.getInstance()));
    }
}