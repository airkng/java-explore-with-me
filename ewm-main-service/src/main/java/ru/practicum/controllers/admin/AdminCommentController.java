package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.response.CommentResponseDto;
import ru.practicum.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCommentController {
    private final CommentService service;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable(value = "commentId") final Long commentId) {
        log.debug("Delete comment with id = {}, by admin.", commentId);
        service.deleteByAdmin(commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> searchComment(@RequestParam final String text,
                                                  @RequestParam(value = "from", required = false, defaultValue = "0") @PositiveOrZero final Integer from,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") @Positive final Integer size
    ) {
        log.debug("Get all comments with params: from = {}, size = {}, text = {}.", from, size, text);
        return service.searchByText(text, from, size);
    }
}
