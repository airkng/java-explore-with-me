package ru.practicum.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionEntity {
    private String reason;
    private String message;
}
