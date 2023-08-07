package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.EventUpdateDto;
import ru.practicum.dto.response.EventFullResponseDto;
import ru.practicum.model.enums.EventState;
import ru.practicum.service.EventService;
import ru.practicum.utils.AdminSearchEventParams;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@Validated
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService service;

    @GetMapping
    public List<EventFullResponseDto> getAllEvents(@RequestParam(required = false) final List<Long> users,
                                                   @RequestParam(required = false) final List<EventState> states,
                                                   @RequestParam(required = false) final List<Long> categories,
                                                   @RequestParam(required = false) final @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                   @RequestParam(required = false) final @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                   @RequestParam(required = false, defaultValue = "0") final @PositiveOrZero Integer from,
                                                   @RequestParam(required = false, defaultValue = "10") final @Positive Integer size
    ) {
        var param = AdminSearchEventParams.builder()
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .categories(categories)
                .states(states)
                .users(users)
                .build();

        log.info("Получение всех событий с заданными параметрами в контроллере EventController");
        return service.getAllFullEvents(param);
    }

    @PatchMapping("/{eventId}")
    public EventFullResponseDto updateEventAndStatus(@RequestBody @Valid final EventUpdateDto updateDto,
                                                     @PathVariable final Long eventId) {
        log.info("Запрос на отклонение/публикацию и изменение события в контроллере EventController");
        return service.updateByAdmin(eventId, updateDto);
    }
}
