package ru.practicum.service;

import ru.practicum.dto.request.ParticipationStatusUpdateRequestDto;
import ru.practicum.dto.response.ParticipationResponseDto;
import ru.practicum.dto.response.ParticipationStatusUpdateResponseDto;

import java.util.List;

public interface EventRequestService {
    List<ParticipationResponseDto> getAll(Long userId);

    ParticipationResponseDto add(Long userId, Long eventId);

    ParticipationResponseDto cancel(Long userId, Long requestId);

    List<ParticipationResponseDto> getAllUserRequests(Long userId, Long eventId);

    ParticipationStatusUpdateResponseDto updateRequestStatus(Long eventId, ParticipationStatusUpdateRequestDto requestDto, Long userId);

}
