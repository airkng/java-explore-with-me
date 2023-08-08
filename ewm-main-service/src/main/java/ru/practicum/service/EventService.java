package ru.practicum.service;

import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.request.EventRequestDto;
import ru.practicum.dto.request.EventUpdateDto;
import ru.practicum.dto.request.search.AdminSearchEventParams;
import ru.practicum.dto.request.search.PubSearchEventParams;
import ru.practicum.dto.response.EventFullResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventFullResponseDto add(EventRequestDto eventRequestDto, Long userId);

    EventFullResponseDto getPublishedById(Long id, HttpServletRequest request);

    List<EventShortDto> getAllPublished(PubSearchEventParams param, HttpServletRequest request);

    EventFullResponseDto getEventByIdAndInitiatorId(Long userId, Long eventId);

    List<EventShortDto> getAllUserId(Long userId, Integer from, Integer size);

    List<EventFullResponseDto> getAllFullEvents(AdminSearchEventParams param);

    EventFullResponseDto updateByAdmin(Long eventId, EventUpdateDto updateDto);

    EventFullResponseDto updateByUser(Long eventId, EventUpdateDto updateDto, Long userId);

}
