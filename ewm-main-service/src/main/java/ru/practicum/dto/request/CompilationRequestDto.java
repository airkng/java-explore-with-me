package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO для создания подборки событий. Применяется при POST/PATCH-запросах в
 * контроллере AdminCompilationController на эндпоинтах /admin/compilations/..
 * events - может быть пустым
 * pinned - закреплена ли запись
 * title - уникальное описание подборки
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class CompilationRequestDto {
    private Set<Long> events = new HashSet<>();

    private Boolean pinned = false;

    /**
     * Описание подборки событий. Параметр должен быть уникальным. Валидация уникальности происходит на уровне
     * сущности Compilation
     */
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
