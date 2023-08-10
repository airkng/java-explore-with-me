package ru.practicum.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.controllers.admin.AdminCompilationController;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * DTO для обновления {@link ru.practicum.model.Compilation Compilation.class}
 * <p>{@link AdminCompilationController AdminCompilationController.class} - <b>основной контроллер, где он используется</b></b></p>
 * <h3>PATCH /admin/compilations/{compId} <b> - эндпоинт </b></h3>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompilationUpdateDto {
    private Set<Long> events;

    private Boolean pinned;

    @Size(min = 1, max = 50)
    private String title;
}
