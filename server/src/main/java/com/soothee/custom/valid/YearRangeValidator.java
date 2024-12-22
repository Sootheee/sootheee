package com.soothee.custom.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class YearRangeValidator implements ConstraintValidator<YearRange, LocalDate> {
    @Override
    public void initialize(YearRange constraintAnnotation) {
        int minYear = constraintAnnotation.minYear();
        int maxYear = constraintAnnotation.maxYear();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
        return !value.isBefore(LocalDate.of(2024, 1, 1))
                && !value.isAfter(LocalDate.of(2100, 12, 31));
    }
}
