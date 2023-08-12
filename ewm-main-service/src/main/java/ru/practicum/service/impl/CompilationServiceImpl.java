package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.controllers.admin.AdminCompilationController;
import ru.practicum.dto.request.CompilationRequestDto;
import ru.practicum.dto.request.CompilationUpdateDto;
import ru.practicum.dto.response.CompilationResponseDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.UniqueViolationException;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CompilationService;
import ru.practicum.utils.PageBuilder;
import ru.practicum.utils.StatsServiceTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final StatsServiceTemplate statsTemplate;
    private final CompilationMapper mapper;

    /**
     * Обращается к БД и ищет всевозможные подборки по заданным критериям
     * Энподинт: /compilations    Контроллер: PublicCompilationController
     *
     * @param pinned закреплена ли подборка
     * @param from   параметр отвечающий за постраничный вывод информации
     * @param size   параметр отвечающий за постраничный вывод. Задает количество сущностей на одной странице
     * @return List CompilationResponseDto
     */
    @Override
    public List<CompilationResponseDto> getAll(final Boolean pinned, final Integer from, final Integer size) {
        Pageable page = PageBuilder.create(from, size);
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

    /**
     * Производит поиск в БД подборки событий по id подборки
     * Эндпоинт: /compilations/{compId}  Контроллер: PublicCompilationController
     *
     * @param id айди подборки (Compilation id)
     * @return CompilationResponseDto подборку событий по заданному id
     */
    @Override
    public CompilationResponseDto getById(Long id) {
        log.info("Получение подборки событий (Публичный контроллер). Сервис CompilationService. Производится запрос к БД");
        var compilation = repository.findById(id).orElseThrow(() -> new NotFoundException("Compilation not found"));
        compilation.setEvents(compilation.getEvents()
                .stream()
                .map(this::setViews)
                .collect(Collectors.toSet()));
        return mapper.toCompilationResponseDto(compilation);
    }

    /**
     * <p><b>Создает подборку, обращаясь к БД. Связывет подборку с реальными событиями.</b></p><br>
     * {@link AdminCompilationController AdminCompilationController.class}  - основной контроллер<br>
     * <h3>/admin/compilations - эндпоинт </h3>
     *
     * @param compilationRequestDto {@link CompilationRequestDto CompilationRequestDto.class} - Dto для работы с сущностью Compilation
     * @return CompilationResponseDto
     * @throws NotFoundException        (Error 404) - if one of events in CompilationRequestDto was not found.
     * @throws UniqueViolationException (Error 409) - if title already exists.
     */
    @Override
    public CompilationResponseDto add(CompilationRequestDto compilationRequestDto) {
        Compilation compilation = mapper.toCompilation(compilationRequestDto);
        try {
            log.info("Установка значений сущности Event в Compilation");
            compilation.setEvents(findEvents(compilationRequestDto.getEvents()));

            log.info("Сохранение Compilation в БД");
            compilation = repository.save(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Title for compilation already exists");
        }
        return mapper.toCompilationResponseDto(compilation);
    }

    /**
     * Превращает Set<> Long айдишников событий (Event) в сущности, обращаясь к БД
     *
     * @param events ids of events
     * @return Set Event
     */
    private Set<Event> findEvents(Set<Long> events) {
        log.info("Поиск сущностей Event в БД");
        if (events == null || events.isEmpty()) {
            return Set.of();
        } else {
            return eventRepository.findAllByIdIn(events).stream()
                    .map(this::setViews)
                    .collect(Collectors.toSet());
        }
    }

    /**
     * Удаляет подборку если она существует {@link Compilation Compilation.class}
     * <p>{@link AdminCompilationController AdminCompilationController.class} - <b>основной контроллер</b></b></p>
     * <h3>DELETE /admin/compilations/{compId} <b> - эндпоинт </b></h3>
     *
     * @param compId id of Compilation entity
     * @throws NotFoundException if Compilation was not found
     */
    @Override
    public void delete(Long compId) {
        log.info("Удаление Compilation из БД");
        if (repository.existsById(compId)) {
            repository.deleteById(compId);
        } else {
            throw new NotFoundException("Compilation not found");
        }
    }

    /**
     * Обновляет подборку событий {@link Compilation Compilation.class}
     * <p>{@link AdminCompilationController AdminCompilationController.class} - <b>основной контроллер</b></b></p>
     * <h3>PATCH /admin/compilations/{compId} <b> - эндпоинт </b></h3>
     *
     * @param compilationUpdateDto {@link CompilationUpdateDto CompilationUpdateDto.class} - dto для обновления сущности
     * @param compId               уникальный идентификатор(id) {@link Compilation}
     * @return {@link CompilationResponseDto CompilationResponseDto.class}
     * @throws UniqueViolationException если title уже существует
     */
    @Override
    @Transactional
    public CompilationResponseDto update(CompilationUpdateDto compilationUpdateDto, Long compId) {
        log.info("Обновление Compilation. Поиск поборки по айди");
        Compilation oldCompilation = repository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));

        if (compilationUpdateDto.getPinned() != null) {
            oldCompilation.setPinned(compilationUpdateDto.getPinned());
        }
        if (compilationUpdateDto.getTitle() != null && !compilationUpdateDto.getTitle().isBlank()) {
            oldCompilation.setTitle(compilationUpdateDto.getTitle());
        }
        if (compilationUpdateDto.getEvents() != null) {
            oldCompilation.setEvents(findEvents(compilationUpdateDto.getEvents()));
            if (oldCompilation.getEvents().size() != compilationUpdateDto.getEvents().size()) {
                throw new NotFoundException("one of events not found");
            }

        }
        try {
            log.info("Сохранение обновленной подборки в БД");
            return mapper.toCompilationResponseDto(repository.save(oldCompilation));
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("Title in compilation already found");
        }
    }

    private Event setViews(Event event) {
        Long views = statsTemplate.getStats(event);
        event.setViews(views);
        return event;
    }

}
