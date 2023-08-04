package ru.practicum.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.request.EventRequestDto;
import ru.practicum.dto.response.EventFullResponseDto;
import ru.practicum.model.Event;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .category(categoryMapper.toCategoryResponseDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .title(event.getTitle())
                .paid(event.getPaid())
                .views(event.getViews())
                .initiator(userMapper.toShortUser(event.getInitiator()))
                .build();
    }

    public EventFullResponseDto toEventResponseDto(Event event) {
        return EventFullResponseDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryResponseDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(userMapper.toShortUser(event.getInitiator()))
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .views(event.getViews())
                .build();
    }

    public Event toEvent(EventRequestDto eventRequestDto) {
        return Event.builder()
                .eventDate(eventRequestDto.getEventDate())
                .annotation(eventRequestDto.getAnnotation())
                .description(eventRequestDto.getDescription())
                .paid(eventRequestDto.getPaid())
                .requestModeration(eventRequestDto.getRequestModeration())
                .participantLimit(eventRequestDto.getParticipantLimit())
                .title(eventRequestDto.getTitle())
                .build();
    }
}
