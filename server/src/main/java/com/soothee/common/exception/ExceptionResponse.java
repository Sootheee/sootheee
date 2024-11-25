package com.soothee.common.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Builder
@Data
public class ExceptionResponse {
    private String msg;
    private String detail;

    public static ResponseEntity<ExceptionResponse> toResponseEntity(MyException ex) {
        MyErrorMsg errorMsg = ex.getErrorMsg();
        String detail = ex.getDetail();

        return ResponseEntity
                .status(ex.getStatus())
                .body(ExceptionResponse.builder()
                        .msg(errorMsg.toString())
                        .detail(detail)
                        .build());
    }
}
