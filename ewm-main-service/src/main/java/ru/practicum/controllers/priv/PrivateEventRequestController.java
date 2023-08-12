package ru.practicum.controllers.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.response.ParticipationResponseDto;
import ru.practicum.service.EventRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateEventRequestController {
    private final EventRequestService service;

    @GetMapping
    public List<ParticipationResponseDto> getAllUserRequest(@PathVariable final Long userId) {
        log.info("Получение заявок на событии конкретного пользователя в контроллере PrivateEventController");
        return service.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationResponseDto addUserRequest(@PathVariable final Long userId,
                                                   @RequestParam(value = "eventId") final Long eventId) {
        log.info("Создание заявки на событие от пользователя в контроллере PrivateEventRequestController");
        return service.add(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationResponseDto cancelResuest(@PathVariable final Long userId,
                                                  @PathVariable final Long requestId) {
        log.info("Отклонение запроса в контроллере PrivateEventRequestController");
        return service.cancel(userId, requestId);
    }

}
