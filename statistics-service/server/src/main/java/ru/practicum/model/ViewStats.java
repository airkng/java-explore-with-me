package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
//конвертация в контроллере из входящих данных
public class ViewStats {

    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;

    private List<String> uris;

    private Boolean unique;
}
