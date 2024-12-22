package com.soothee.custom.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BindingErrorResult {
    private String objectName;
    private String fieldName;
    private String code;
    private String errorMessage;

    public BindingErrorResult(FieldError fieldError) {
        this.objectName = fieldError.getObjectName();
        this.fieldName = fieldError.getField();
        this.code = fieldError.getCode();
        this.errorMessage = fieldError.getDefaultMessage();
    }
}
