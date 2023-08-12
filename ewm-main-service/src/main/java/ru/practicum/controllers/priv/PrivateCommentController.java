package ru.practicum.controllers.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.CommentRequestDto;
import ru.practicum.dto.response.CommentResponseDto;
import ru.practicum.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateCommentController {
    private final CommentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(@PathVariable(value = "userId") final Long userId,
                                         @Valid @RequestBody final CommentRequestDto commentDto,
                                         @RequestParam(value = "eventId") final Long eventId) {
        log.debug("Create comment by user. Params: user id = '{}', event id = '{}', body = {}", userId, eventId, commentDto);
        return service.add(userId, eventId, commentDto);
    }

    @GetMapping
    public List<CommentResponseDto> getAllCommentByUser(@PathVariable(value = "userId") final Long userId,
                                                        @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero final Integer from,
                                                        @RequestParam(value = "size", required = false, defaultValue = "10") @Positive final Integer size) {
        log.debug("Get all comments by user. Params: user id = '{}', from = '{}', size = '{}'.", userId, from, size);
        return service.getAllByUser(userId, from, size);
    }

    @PatchMapping("/{commentId}")
    public CommentResponseDto updateComment(@PathVariable(value = "userId") final Long userId,
                                            @PathVariable(value = "commentId") final Long commentId,
                                            @Valid @RequestBody final CommentRequestDto commentDto) {
        log.debug("Update comment by user. Params: user id = '{}', comment id = '{}', body = {}", userId, commentId, commentDto);
        return service.update(userId, commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(value = "userId") final Long userId,
                              @PathVariable(value = "commentId") final Long commentId) {
        log.debug("Delete comment by user. Params: user id = '{}', comment id = '{}'.", userId, commentId);
        service.delete(userId, commentId);
    }
}
