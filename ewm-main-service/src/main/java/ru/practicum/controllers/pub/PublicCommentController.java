package ru.practicum.controllers.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.response.CommentResponseDto;
import ru.practicum.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCommentController {
    private final CommentService service;

    @GetMapping
    public List<CommentResponseDto> getAllFromEvent(@PathVariable(value = "eventId") final Long eventId,
                                                         @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero final Integer from,
                                                         @RequestParam(value = "size", required = false, defaultValue = "10") @Positive final Integer size) {
        log.debug("Get all comments by Event. Params: event id = '{}', from = '{}', size = '{}'.", eventId, from, size);
        return service.getAllFromEvent(eventId, from, size);
    }
}
