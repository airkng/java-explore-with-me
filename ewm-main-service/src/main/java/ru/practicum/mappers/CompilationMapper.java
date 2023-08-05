package ru.practicum.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.request.CompilationRequestDto;
import ru.practicum.dto.request.CompilationUpdateDto;
import ru.practicum.dto.response.CompilationResponseDto;
import ru.practicum.model.Compilation;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public CompilationResponseDto toCompilationResponseDto(Compilation compilation) {
        return CompilationResponseDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream()
                        .map(eventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .build();

    }

    public Compilation toCompilation(CompilationRequestDto compilationRequestDto) {
        return Compilation.builder()
                .pinned(compilationRequestDto.getPinned())
                .title(compilationRequestDto.getTitle())
                .build();
    }

    public Compilation toCompilation(CompilationUpdateDto compilationUpdateDto) {
        return Compilation.builder()
                .pinned(compilationUpdateDto.getPinned())
                .title(compilationUpdateDto.getTitle())
                .build();
    }
}
