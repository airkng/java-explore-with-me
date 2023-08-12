package ru.practicum.service;

import ru.practicum.dto.request.CommentRequestDto;
import ru.practicum.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {
    List<CommentResponseDto> getAllFromEvent(Long eventId, Integer from, Integer size);

    List<CommentResponseDto> getAllByUser(Long userId, Integer from, Integer size);

    List<CommentResponseDto> searchByText(String text, Integer from, Integer size);

    CommentResponseDto add(Long userId, Long eventId, CommentRequestDto commentDto);

    CommentResponseDto update(Long userId, Long commentId, CommentRequestDto commentDto);

    void deleteByAdmin(Long commentId);

    void delete(Long userId, Long commentId);
}
