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
/**
 * Дто возвращающее информацию о заявке пользователя на событие
 * Используется в PrivEventRequestController POST/GET/PATCH методах
 */
public class ParticipationResponseDto {
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private ParticipationRequestState status;
}
