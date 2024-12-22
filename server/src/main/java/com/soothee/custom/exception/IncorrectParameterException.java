package com.soothee.custom.exception;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.CustomType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IncorrectParameterException extends ClassNotFoundException{
    public IncorrectParameterException(Class<? extends CustomType> classType, String value) {
        super(value + (Objects.equals(classType, ContentType.class) ? "은 type path paremeter의 올바른 값이 아닙니다." : "은 order_by query parameter의 올바른 값이 아닙니다."));
    }
}
