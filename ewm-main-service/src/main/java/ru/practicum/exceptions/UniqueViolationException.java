package ru.practicum.exceptions;

public class UniqueViolationException extends RuntimeException {
    public UniqueViolationException(String message) {
        super(message);
    }
}
