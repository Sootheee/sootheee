package com.soothee.custom.exception;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.CustomType;
import com.soothee.common.constants.ReferenceType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IncorrectParameterException extends RuntimeException{
    public IncorrectParameterException(Class<? extends CustomType> classType, String value) {
        super(value + (Objects.equals(classType, ContentType.class) ? "은 type path paremeter의 올바른 값이 아닙니다."
                : (Objects.equals(classType, ReferenceType.class) ? "는 올바르지 않는 일련번호 형식입니다."
                : "은 order_by query parameter의 올바른 값이 아닙니다.")));
    }

    public IncorrectParameterException(String value) {
        super("query parameter {" + value + "}의 값이 \"name\"이 아닙니다.");
    }
}
