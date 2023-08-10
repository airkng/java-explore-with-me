package ru.practicum.controllers.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.request.search.PubSearchEventParams;
import ru.practicum.dto.response.EventFullResponseDto;
import ru.practicum.model.enums.EventSort;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PubEventController {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getAllPublishedEvents(@RequestParam(required = false, value = "text") final String text,
                                                     @RequestParam(required = false, value = "categories") final List<Long> categories,
                                                     @RequestParam(required = false, value = "paid") final Boolean paid,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") final LocalDateTime rangeStart,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") final LocalDateTime rangeEnd,
                                                     @RequestParam(required = false, defaultValue = "false") final Boolean onlyAvailable,
                                                     @RequestParam(required = false) final EventSort sort,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero final Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive final Integer size,
                                                     final HttpServletRequest request) {
        log.info("получение опубликованных событий по параметрам PubEventController");
        var params = PubSearchEventParams.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();
        return service.getAllPublished(params, request);

    }

    @GetMapping("/{id}")
    public EventFullResponseDto getPublishedEventById(@PathVariable final Long id, final HttpServletRequest request) {
        log.info("Получение опубликованного события в контроллере PubEventController");
        return service.getPublishedById(id, request);
    }

}
