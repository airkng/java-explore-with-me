package ru.practicum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.enums.ParticipationRequestState;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class EventRequestResponseDto {
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private ParticipationRequestState status;
}
