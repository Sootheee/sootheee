package com.soothee.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MyException.class)
    protected ResponseEntity<ExceptionResponse> handleCustom400Exception(MyException ex) {
        return ExceptionResponse.toResponseEntity(ex);
    }
}
