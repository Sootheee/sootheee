package com.soothee.custom.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.util.Objects;

public class YearRangeValidator implements ConstraintValidator<YearRange, LocalDate> {
    private int minYear;
    private int maxYear;

    @Override
    public void initialize(YearRange constraintAnnotation) {
        this.minYear = constraintAnnotation.minYear();
        this.maxYear = constraintAnnotation.maxYear();
    }

    /**
     * 날짜 범위 검증
     * 입력된 날짜가
     * 1. null 이 아니고
     * 2. 2024년 이전이 아니고
     * 3. 2100년 이후가 아닌지 확인
     * 검증
     *
     * @param value 입력된 날짜
     * @param constraintValidatorContext ConstraintValidatorContext
     * @return 맞으면 true / 아니면 false
     */
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.nonNull(value)
                && !value.isBefore(LocalDate.of(minYear, 1, 1))
                && !value.isAfter(LocalDate.of(maxYear, 12, 31));
    }
}
