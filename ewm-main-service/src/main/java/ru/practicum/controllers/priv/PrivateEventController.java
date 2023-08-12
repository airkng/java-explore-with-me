package ru.practicum.controllers.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.request.EventRequestDto;
import ru.practicum.dto.request.EventUpdateDto;
import ru.practicum.dto.request.ParticipationStatusUpdateRequestDto;
import ru.practicum.dto.response.EventFullResponseDto;
import ru.practicum.dto.response.ParticipationResponseDto;
import ru.practicum.dto.response.ParticipationStatusUpdateResponseDto;
import ru.practicum.service.EventRequestService;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@Slf4j
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService service;
    private final EventRequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllUserEvents(@PathVariable(name = "userId") final Long userId,
                                                @RequestParam(name = "from", defaultValue = "0", required = false) @PositiveOrZero final Integer from,
                                                @RequestParam(name = "size", defaultValue = "10", required = false) @Positive final Integer size) {
        log.info("Запрос на получение всех событий. Возвращается лист ShortEvent");
        return service.getAllUserId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullResponseDto getEventById(@PathVariable(name = "userId") final Long userId,
                                             @PathVariable(name = "eventId") final Long eventId) {
        log.info("Запрос на получение конкретного события, добавленное текущим пользователем");
        return service.getEventByIdAndInitiatorId(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationResponseDto> getAllUserParticipationRequests(@PathVariable(name = "userId") final Long userId,
                                                                          @PathVariable(name = "eventId") final Long eventId) {
        log.info("Запрос на получение информации о запросах на участие в событии текущего пользователя");
        return requestService.getAllUserRequests(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullResponseDto addEvent(@PathVariable(name = "userId") final Long userId,
                                         @RequestBody @Valid EventRequestDto eventRequestDto) {
        log.info("Добавление события в контроллер PrivateEventController");
        return service.add(eventRequestDto, userId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullResponseDto updateEvent(@PathVariable(name = "userId") final Long userId,
                                            @PathVariable(name = "eventId") final Long eventId,
                                            @RequestBody @Valid final EventUpdateDto updateDto) {
        log.info("обновление события в private контроллере");
        return service.updateByUser(eventId, updateDto, userId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationStatusUpdateResponseDto updateEventRequestStatus(@RequestBody final ParticipationStatusUpdateRequestDto requestDto,
                                                                         @PathVariable final Long eventId,
                                                                         @PathVariable final Long userId) {
        log.info("Изменение статуса(подтверждена/отклонена) на участие в событии текущего пользователя в контроллере PrivateEventController");
        return requestService.updateRequestStatus(eventId, requestDto, userId);
    }
}
