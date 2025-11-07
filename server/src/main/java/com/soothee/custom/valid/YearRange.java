package com.soothee.custom.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = YearRangeValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface YearRange {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int minYear() default 2024;
    int maxYear() default 2100;
}
