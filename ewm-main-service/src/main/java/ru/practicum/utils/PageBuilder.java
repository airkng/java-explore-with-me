package ru.practicum.utils;

import org.springframework.data.domain.PageRequest;

public class PageBuilder {
    public static PageRequest create(final Integer from, final Integer size) {
        return PageRequest.of(from / size, size);
    }
}
