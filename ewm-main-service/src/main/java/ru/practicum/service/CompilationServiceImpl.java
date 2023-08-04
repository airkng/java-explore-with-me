package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CompilationUpdateDto;
import ru.practicum.dto.request.CompilationRequestDto;
import ru.practicum.dto.response.CompilationResponseDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.UniqueViolationException;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService{
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;

    @Override
    public List<CompilationResponseDto> getAll(Boolean pinned, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        List<Compilation> compilations;
        if (pinned) {
            log.info("Запрос на получение прикрепленных подборок. Выполнение в CompilationService. Производится запрос в БД");
            compilations = repository.findAllByPinned(pinned, page);
        } else {
            log.info("Запрос на получение НЕприкрепленных подборок. Выполнение в CompilationService. Производится запрос в БД");
            compilations = repository.findAll(page).getContent();
        }

        if (compilations.isEmpty()) {
            log.info("Подборки не найдены. Возращение пустого списка");
            return List.of();
        }

        return compilations.stream()
                .map(mapper::toCompilationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationResponseDto getById(Long id) {
        log.info("Получение подборки событий (Публичный контроллер). Сервис CompilationService. Производится запрос к БД");
        return mapper.toCompilationResponseDto(repository.findById(id).orElseThrow(()  -> new NotFoundException("Compilation not found")));
    }

    @Override
    public CompilationResponseDto add(CompilationRequestDto compilationRequestDto) {
        Compilation compilation = mapper.toCompilation(compilationRequestDto);
        try {
            compilation.setEvents(findEvents(compilationRequestDto.getEvents()));
            compilation = repository.save(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Title for compilation already exists");
        }
        return mapper.toCompilationResponseDto(compilation);
    }

    private Set<Event> findEvents(Set<Long> events) {
        if (events.isEmpty()) {
            return Set.of();
        } else {
           return eventRepository.findAllByIdIn(events);
        }
    }

    @Override
    public void delete(Long compId) {
        if (repository.existsById(compId)) {
            repository.deleteById(compId);
        } else {
            throw new NotFoundException("Compilation not found");
        }
    }

    @Override
    @Transactional
    public CompilationResponseDto update(CompilationUpdateDto compilationUpdateDto, Long compId) {
        Compilation oldCompilation = repository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));
        //Compilation newCompilation = mapper.toCompilation(compilationUpdateDto);

        if (compilationUpdateDto.getPinned() != null) {
            oldCompilation.setPinned(compilationUpdateDto.getPinned());
        }
        if (compilationUpdateDto.getTitle() != null && !compilationUpdateDto.getTitle().isBlank()) {
            oldCompilation.setTitle(compilationUpdateDto.getTitle());
        }
        if (compilationUpdateDto.getEvents() != null) {
            oldCompilation.setEvents(findEvents(compilationUpdateDto.getEvents()));
        }
        try {
            return mapper.toCompilationResponseDto(repository.save(oldCompilation));
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Title in compilation already found");
        }

    }

}
