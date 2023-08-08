package ru.practicum.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateValidate implements ConstraintValidator<CorrectDateNullable, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.isAfter(LocalDateTime.now().plusHours(2));
    }
}
