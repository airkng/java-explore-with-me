package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionsHandlers {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionEntity handleConstraintViolationException(ConstraintViolationException e) {
        return new ExceptionEntity(e.getMessage(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionEntity handleConflictException(ConflictException e) {
        return new ExceptionEntity(e.getMessage(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionEntity handleAlreadyExistsException(AlreadyExistsException e) {
        return new ExceptionEntity(e.getMessage(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionEntity handleUniqueViolationException(UniqueViolationException e) {
        return new ExceptionEntity(e.getMessage(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionEntity handleNotFoundException(NotFoundException e) {
        return new ExceptionEntity(e.getMessage(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionEntity handleBadRequestException(BadRequestException e) {
        return new ExceptionEntity(e.getMessage(), e.getMessage());
    }
}
