package ru.practicum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.UserShortDto;
import ru.practicum.model.Location;
import ru.practicum.model.enums.EventState;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class EventFullResponseDto {
    private String annotation;
    private CategoryResponseDto category;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    //предварительно локэйшн оставим без дто
    private Location location;
    //TODO:возможно придется везде заменить на Integer
    private Long participantLimit;
    //TODO:возможно изменить формат на другой
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private Long views;

}
