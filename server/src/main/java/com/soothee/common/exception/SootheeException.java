package com.soothee.common.exception;

public class SootheeException extends RuntimeException{
    public SootheeException() {
        super();
    }

    public SootheeException(MyErrorMsg msg) {
        super(msg.toString());
    }

    public SootheeException(String msg) {
        super(msg);
    }
}
