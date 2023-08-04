package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class CompilationUpdateDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Long> events;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean pinned;

    @Size(min = 1, max = 50)
    private String title;
}
