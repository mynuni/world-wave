package com.my.worldwave.exception;

import com.my.worldwave.exception.auth.AuthenticationFailureException;
import com.my.worldwave.exception.member.RefreshTokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getClass().getSimpleName(), e.getMessage()));
    }

    // 바인딩 에러 한 개만 반환
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        FieldError firstFieldError = fieldErrors.get(0);
        ExceptionResponse response = new ExceptionResponse(e.getClass().getSimpleName(), firstFieldError.getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //     바인딩 에러를 리스트로 반환할 경우
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<List<ExceptionResponse>> handleValidationException(MethodArgumentNotValidException e) {
//        BindingResult bindingResult = e.getBindingResult();
//        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//        List<ExceptionResponse> responses = fieldErrors.stream()
//                .map(fieldError -> new ExceptionResponse(e.getClass().getSimpleName(), fieldError.getDefaultMessage()))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responses);
//    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(AuthenticationFailureException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationFailureException(AuthenticationFailureException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleRefreshTokenExpiredException(RefreshTokenExpiredException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(e.getClass().getSimpleName(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleInternalServerException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(e.getClass().getSimpleName(), e.getMessage()));
    }

}
