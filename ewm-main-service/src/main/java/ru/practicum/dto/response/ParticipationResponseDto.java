package ru.practicum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.enums.ParticipationState;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class ParticipationResponseDto {
    //TODO: возможно будет нужна будет исправить формат вывода
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private ParticipationState status;
}
