package com.rs.shopvn.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class AgeRangeValidator implements ConstraintValidator<AgeRange, String> {
    private int min;
    private int maxExclusive;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    @Override
    public void initialize(AgeRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.maxExclusive = constraintAnnotation.maxExclusive();
    }

    @Override
    public boolean isValid(String dob, ConstraintValidatorContext context) {
        if (dob == null || dob.isBlank()) return true; // optional

        try {
            LocalDate birth = LocalDate.parse(dob, FMT);
            LocalDate today = LocalDate.now();
            if (birth.isAfter(today)) return false;

            int age = Period.between(birth, today).getYears();
            return age >= min && age < maxExclusive;
        } catch (Exception e) {
            // format đã check bằng regex, nhưng vẫn phòng parse strict
            return false;
        }
    }
}