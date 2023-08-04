package ru.practicum.exceptions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateValidate implements ConstraintValidator<CorrectDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return value.isAfter(LocalDateTime.now().plusHours(2));
    }
}
