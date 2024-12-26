package com.soothee.custom.exception;

import com.soothee.common.constants.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IncorrectValueException extends RuntimeException {
    public IncorrectValueException(DoubleType type, Double value) {
        super(type.toKorean() + " 값이 {" + value + "}으로 0을 포함한 양수가 아닙니다.");
    }

    public IncorrectValueException(LocalDate value) {
        super(StringType.DATE.toKorean() +"가 {" + value + "}으로 2024년에서 2100년 사이가 아닙니다.");
    }

    public IncorrectValueException(Integer value) {
        super(StringType.ORDER_NO.toKorean() + "가 {" + value + "}으로 0을 포함한 양수가 아닙니다.");
    }

    public IncorrectValueException(CustomType type, Integer value) {
        super(type.toKorean() + " 갯수가 {" + value + "}으로 0을 포함한 양수가 아닙니다.");
    }

    public IncorrectValueException(DomainType type, Long value) {
        super(type.toKorean() + "의 일련번호 {" + value + "}가 양수가 아닙니다.");
    }

    public IncorrectValueException(BooleanType type, String value) {
        super(type.toKorean() + "의 값 {" + value + "}가 Y나 N이 아닙니다");
    }

    public IncorrectValueException(String value) {
        super(StringType.EMAIL.toKorean() + "인 {" + value + "}의 이메일 형식이 잘못되었습니다.");
    }

    public IncorrectValueException(CustomType type, String value, int min, int max) {
        super(type.toKorean() + "의 값 {" + value + "}이 " + min + "와 " + max + "사이가 아닙니다.");
    }
}
