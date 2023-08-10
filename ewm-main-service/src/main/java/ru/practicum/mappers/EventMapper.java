package ru.practicum.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.request.EventRequestDto;
import ru.practicum.dto.response.EventFullResponseDto;
import ru.practicum.model.Event;

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
                .id(event.getId())
                .participantLimit(event.getParticipantLimit())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .title(event.getTitle())
                .paid(event.getPaid())
                .requestModeration(event.getRequestModeration())
                .createdOn(event.getCreatedOn())
                .eventDate(event.getEventDate())
                .publishedOn(event.getPublishedOn())
                .category(categoryMapper.toCategoryResponseDto(event.getCategory()))
                .location(event.getLocation())
                .initiator(userMapper.toShortUser(event.getInitiator()))
                .state(event.getState())
                .build();
    }

    public Event toEvent(EventRequestDto eventRequestDto) {
        return Event.builder()
                .annotation(eventRequestDto.getAnnotation())
                .title(eventRequestDto.getTitle())
                .description(eventRequestDto.getDescription())
                .location(eventRequestDto.getLocation())
                .eventDate(eventRequestDto.getEventDate())
                .participantLimit(eventRequestDto.getParticipantLimit())
                .paid(eventRequestDto.getPaid())
                .requestModeration(eventRequestDto.getRequestModeration())
                .build();
    }
}
