package com.ducbao.service_be.exception;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public <T> ResponseEntity<ResponseDto<T>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Exception occured", e);
        final ResponseDto<T> dto = ResponseDto.<T>builder()
                .success(false)
                .message(e.getMessage())
                .statusCode(StatusCodeEnum.EXCEPTION0507.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler(value = Exception.class)
    public <T> ResponseEntity<ResponseDto<T>> handleException(Exception e) {
        log.error("Exception occured", e);
        final ResponseDto<T> dto = ResponseDto.<T>builder()
                .success(false)
                .message(e.getMessage())
                .statusCode(StatusCodeEnum.EXCEPTION0400.toString())
                .build();
        return ResponseEntity.ok(dto);
    }
    @ExceptionHandler(value = AuthenticationException.class)
    public <T> ResponseEntity<ResponseDto<T>> handleAuthenticationException(AuthenticationException e) {
        log.error("Exception occured", e);
        final ResponseDto<T> dto = ResponseDto.<T>builder()
                .success(false)
                .message(e.getMessage())
                .statusCode(StatusCodeEnum.EXCEPTION1001.toString())
                .build();
        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler(value = ClassCastException.class)
    public <T> ResponseEntity<ResponseDto<T>> handleClassCastException(ClassCastException e) {
        log.error("ClassCastException occurred", e);
        final ResponseDto<T> dto = ResponseDto.<T>builder()
                .success(false)
                .message("Lỗi ép kiểu dữ liệu: " + e.getMessage())
                .statusCode(StatusCodeEnum.EXCEPTION1001.toString()) // Thêm mã lỗi mới nếu cần
                .build();
        return ResponseEntity.ok(dto);
    }
}
