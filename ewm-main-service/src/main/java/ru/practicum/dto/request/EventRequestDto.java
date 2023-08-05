package ru.practicum.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.exceptions.CorrectDate;
import ru.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class EventRequestDto {
    @NotBlank //1
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotBlank //2
    @Size(min = 3, max = 120)
    private String title;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    private Long category;

    @NotNull
    private Location location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @CorrectDate
    //TODO:мб убрать CorrectDate, вместо этого добавить проверку в сервисе
    private LocalDateTime eventDate;

    private Long participantLimit;

    private Boolean paid;

    private Boolean requestModeration;




}
