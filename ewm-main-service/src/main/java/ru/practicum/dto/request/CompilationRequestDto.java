package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data

/**
 * Дто для создания подборки событий. Применяется при POST/PATCH-запросах в
 * контроллере CompilationController на эндпоинтах /admin/compilations/..
 * events - может быть пустым
 * pinned - закреплена ли запись
 * title - уникальное описание подборки
 */
public class CompilationRequestDto {
    private Set<Long> events;

    private Boolean pinned;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
