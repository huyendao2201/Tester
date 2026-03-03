package com.rs.shopvn.validation;

import com.rs.shopvn.dto.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterRequest> {
    @Override
    public boolean isValid(RegisterRequest value, ConstraintValidatorContext context) {
        if (value == null) return true;
        if (value.getPassword() == null || value.getConfirmPassword() == null) return true;

        boolean ok = value.getPassword().equals(value.getConfirmPassword());
        if (!ok) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Xác nhận mật khẩu phải trùng khớp mật khẩu.")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }
        return ok;
    }
}