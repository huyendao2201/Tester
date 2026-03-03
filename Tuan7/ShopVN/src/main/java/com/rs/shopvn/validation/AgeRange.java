package com.rs.shopvn.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeRangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeRange {
    int min();
    int maxExclusive();
    String message() default "Tuổi không hợp lệ.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}