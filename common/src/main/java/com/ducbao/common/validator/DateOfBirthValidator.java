package com.ducbao.common.validator;

import com.ducbao.common.anotation.IsDateOfBirth;
import com.ducbao.common.util.DateUtil;
import com.ducbao.common.util.Util;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateOfBirthValidator implements ConstraintValidator<IsDateOfBirth, String> {

    @Override
    public void initialize(IsDateOfBirth constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (Util.isNullOrEmpty(s)) {
            return true;
        }
        try {
            LocalDate date = DateUtil.convertDateResponseToLocalDate(s, DateUtil.DATE_FORMAT);
            if (date.getYear() <= 1900) {
                return false;
            }

            if (date.isAfter(LocalDate.now())) {
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
