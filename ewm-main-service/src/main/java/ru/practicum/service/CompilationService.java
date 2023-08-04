package ru.practicum.service;

import ru.practicum.dto.CompilationUpdateDto;
import ru.practicum.dto.request.CompilationRequestDto;
import ru.practicum.dto.response.CompilationResponseDto;

import java.util.List;

public interface CompilationService {
    List<CompilationResponseDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationResponseDto getById(Long id);

    CompilationResponseDto add(CompilationRequestDto compilationRequestDto);

    void delete(Long compId);

    CompilationResponseDto update(CompilationUpdateDto compilationUpdateDto, Long compId);
}
