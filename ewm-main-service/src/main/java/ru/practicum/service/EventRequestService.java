package ru.practicum.service;

import ru.practicum.dto.response.EventRequestResponseDto;
import ru.practicum.dto.response.ParticipationResponseDto;

import java.util.List;

public interface EventRequestService {
    List<EventRequestResponseDto> getAll(Long userId);

    EventRequestResponseDto add(Long userId, Long eventId);

    EventRequestResponseDto cancel(Long userId, Long requestId);

    List<EventRequestResponseDto> getAllUserRequests(Long userId, Long eventId);

}
