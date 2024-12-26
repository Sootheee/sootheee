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

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
                /* 입력 값이 null 이 아니고 */
        return Objects.nonNull(value)
                /* 입력 값이 2024년 이전이 아니고*/
                && !value.isBefore(LocalDate.of(minYear, 1, 1))
                /* 입력 값이 2100년 이후가 아닌지 확인 */
                && !value.isAfter(LocalDate.of(maxYear, 12, 31));
    }
}
