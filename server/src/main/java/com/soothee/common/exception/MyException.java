package com.soothee.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MyException extends RuntimeException {
    private final HttpStatus status;
    private final MyErrorMsg errorMsg;
    private final String detail;

    public MyException(HttpStatus status, MyErrorMsg errorMsg) {
        this.status = status;
        this.errorMsg = errorMsg;
        this.detail = "";
    }

    public MyException(HttpStatus status, MyErrorMsg errorMsg, String detail) {
        this.status = status;
        this.errorMsg = errorMsg;
        this.detail = detail;
    }

    public MyException(HttpStatus status, MyErrorMsg errorMsg, Throwable cause) {
        this.status = status;
        this.errorMsg = errorMsg;
        this.detail = cause.getMessage();
    }

    public MyException(HttpStatus status, MyException myException) {
        this.status = status;
        this.errorMsg = myException.getErrorMsg();
        this.detail = myException.getDetail();
    }

    public MyException(HttpStatus status, Throwable cause) {
        this.status = status;
        this.errorMsg = null;
        this.detail = cause.getMessage();
    }

    public MyException(Exception exception) {
        if (exception.getClass() == MyException.class) {
            MyException myException = (MyException) exception;
            this.status = myException.getStatus();
            this.errorMsg = myException.getErrorMsg();
            this.detail = myException.getMessage();
        } else {
            this.status = HttpStatus.BAD_REQUEST;
            this.errorMsg = null;
            this.detail = exception.getMessage();
        }
    }
}
