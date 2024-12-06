package com.ducbao.common.validator;

import com.ducbao.common.anotation.IsEmail;
import com.ducbao.common.util.EmailUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsEmailValidator implements ConstraintValidator<IsEmail, String> {
    @Override
    public void initialize(IsEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        return EmailUtils.isEmail(s);
    }
}
