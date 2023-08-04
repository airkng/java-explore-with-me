package ru.practicum.service;

import org.springframework.data.domain.Sort;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.EventUpdateDto;
import ru.practicum.dto.request.EventRequestDto;
import ru.practicum.dto.request.EventRequestStatusUpdateRequestDto;
import ru.practicum.dto.response.EventRequestResponseDto;
import ru.practicum.dto.response.EventFullResponseDto;
import ru.practicum.dto.response.ParticipationResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullResponseDto add(EventRequestDto eventRequestDto, Long userId);

    EventFullResponseDto getEventByIdAndInitiatorId(Long userId, Long eventId);

    List<EventShortDto> getAll(Long userId, Integer from, Integer size);

    EventFullResponseDto update(Long eventId, EventUpdateDto updateDto, Long userId);

    List<ParticipationResponseDto> getAllUserRequests(Long userId, Long eventId);

    EventRequestResponseDto updateRequestStatus(Long eventId, EventRequestStatusUpdateRequestDto requestDto, Long userId);

    List<EventShortDto> getAllPublished(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, Sort sort, Integer from, Integer size, HttpServletRequest request);

    EventShortDto getPublishedById(Long id, HttpServletRequest request);

    EventFullResponseDto update(Long eventId, EventUpdateDto updateDto);

    List<EventFullResponseDto> getAllFullEvents(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size, HttpServletRequest request);

}
