package ru.practicum.exceptions;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Negative;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DateValidate.class)
public @interface CorrectDate {
    String message() default "{Incorrect.event.date!}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
