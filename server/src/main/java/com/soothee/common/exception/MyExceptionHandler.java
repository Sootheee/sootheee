package com.soothee.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(MyException.class)
    protected ResponseEntity<ErrorDTO> handleCustom400Exception(MyException ex) {
        return ErrorDTO.toResponseEntity(ex);
    }
}
