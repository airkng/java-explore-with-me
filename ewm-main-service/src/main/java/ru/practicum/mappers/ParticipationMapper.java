package ru.practicum.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.dto.response.ParticipationResponseDto;
import ru.practicum.model.Event;
import ru.practicum.model.Participation;
import ru.practicum.model.User;
import ru.practicum.model.enums.ParticipationState;

import java.time.LocalDateTime;

@Component
public class ParticipationMapper {
    public ParticipationResponseDto toParticipationResponseDto(Participation participation) {
        return ParticipationResponseDto.builder()
                .requester(participation.getRequester().getId())
                .event(participation.getEvent().getId())
                .id(participation.getId())
                .created(participation.getCreated())
                .status(participation.getStatus())
                .build();
    }

    public Participation toParticipation(Event event, User user) {
        return Participation.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(event.getRequestModeration() ? ParticipationState.PENDING : ParticipationState.CONFIRMED)
                .build();
    }
}
