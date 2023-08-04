package ru.practicum.mappers;

import ru.practicum.dto.response.EventRequestResponseDto;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.model.enums.ParticipationRequestState;

import java.time.LocalDateTime;

public class ParticipationRequestMapper {
    public EventRequestResponseDto toEventRequestResponseDto(ParticipationRequest participationRequest) {
        return EventRequestResponseDto.builder()
                .requester(participationRequest.getRequester().getId())
                .event(participationRequest.getEvent().getId())
                .id(participationRequest.getId())
                .created(participationRequest.getCreated())
                .status(participationRequest.getStatus())
                .build();
    }

    public ParticipationRequest toParticipationRequest(Event event, User user) {
        return ParticipationRequest.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(event.getRequestModeration() ? ParticipationRequestState.PENDING : ParticipationRequestState.CONFIRMED)
                .build();
    }
}
