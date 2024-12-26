package com.soothee.custom.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistReferenceIdValidator.class)
@Target({ElementType.TYPE_USE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistReferenceId {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int idLength() default 7;
    int min();
    int max();
}
