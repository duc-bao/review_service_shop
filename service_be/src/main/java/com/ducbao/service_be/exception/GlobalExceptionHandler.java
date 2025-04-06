package com.ducbao.service_be.exception;

import com.ducbao.common.model.dto.ResponseDto;
import com.ducbao.common.model.enums.StatusCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.rmi.AccessException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public <T> ResponseEntity<ResponseDto<T>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation error occurred", e);

        BindingResult bindingResult = e.getBindingResult();

        // Lấy tất cả lỗi của các trường và ghép thành một message
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ResponseDto<T> dto = ResponseDto.<T>builder()
                .success(false)
                .message(errorMessage) // Sử dụng message đã được xử lý
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
    @ExceptionHandler(value = IncorrectResultSizeDataAccessException.class)
    public <T> ResponseEntity<ResponseDto<T>> handleIncorrectResultSizeDataAccessException(ClassCastException e) {
        log.error("ClassCastException occurred", e);
        final ResponseDto<T> dto = ResponseDto.<T>builder()
                .success(false)
                .message("Lỗi data: " + e.getMessage())
                .statusCode(StatusCodeEnum.EXCEPTION1001.toString()) // Thêm mã lỗi mới nếu cần
                .build();
        return ResponseEntity.ok(dto);
    }
//    @ExceptionHandler(value = AccessDeniedException.class)
//    public <T> ResponseEntity<ResponseDto<T>> handleAccesDenied(ClassCastException e) {
//        log.error("AccessDenied occurred", e);
//        final ResponseDto<T> dto = ResponseDto.<T>builder()
//                .success(false)
//                .message("Bạn không thể truy cập chức năng này " )
//                .statusCode(StatusCodeEnum.EXCEPTION1001.toString()) // Thêm mã lỗi mới nếu cần
//                .build();
//        return ResponseEntity.ok(dto);
//    }
}
