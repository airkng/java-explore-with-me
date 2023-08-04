package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.EventUpdateDto;
import ru.practicum.dto.request.EventRequestDto;
import ru.practicum.dto.request.EventRequestStatusUpdateRequestDto;
import ru.practicum.dto.response.EventRequestResponseDto;
import ru.practicum.dto.response.EventFullResponseDto;
import ru.practicum.dto.response.ParticipationResponseDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;
import ru.practicum.model.enums.EventState;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventMapper mapper;

    @Override
    @Transactional
    public EventFullResponseDto add(EventRequestDto eventRequestDto, Long userId) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        Event event = mapper.toEvent(eventRequestDto);
        Location location = locationRepository.save(event.getLocation());
        Category category = categoryRepository.findById(eventRequestDto.getCategory()).orElseThrow(() -> new NotFoundException("category not found"));

        event.setLocation(location);
        event.setInitiator(initiator);
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(category);
        event.setState(EventState.PENDING);
        event.setViews(0L);
        return mapper.toEventResponseDto(repository.save(event));
    }

    /**
     * Приватный метод энодпоинта /users/{userId}/events/{events} контроллера PrivEventController
     * Возращает полное описание Event, если userId является владельцем Event
     * 404 - пользователь не найден / userId не совпадает с Initiator id
     * @param userId айди пользователя, который запрашивает
     * @param eventId айди события
     * @return EventFullResponseDto
     */
    @Override
    public EventFullResponseDto getEventByIdAndInitiatorId(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("user not found");
        }
        return mapper.toEventResponseDto(repository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new NotFoundException("event not found")));
    }

    /**
     * Получение List событий, добавленных текущим пользователем эндпоинта /users/{userId}/events контроллера PrivEventController
     * @param userId id владельца события Event
     * @param from для постраничной загрузки
     * @param size для постраничной загрузки
     * @return возращает EventShortDto
     */
    @Override
    public List<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("user not found");
        }
        List<Event> events = repository.findAllByInitiatorId(userId, page);
        if (events.isEmpty()) {
            return List.of();
        }
        return events.stream()
                .map(mapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullResponseDto update(Long eventId, EventUpdateDto updateDto, Long userId) {
        return null;
    }

    @Override
    public List<ParticipationResponseDto> getAllUserRequests(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventRequestResponseDto updateRequestStatus(Long eventId, EventRequestStatusUpdateRequestDto requestDto, Long userId) {
        return null;
    }

    @Override
    public List<EventShortDto> getAllPublished(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, Sort sort, Integer from, Integer size, HttpServletRequest request) {
        //TODO: здесь нужно применить сервис статистики
        return null;
    }

    @Override
    public EventShortDto getPublishedById(Long id, HttpServletRequest request) {
        //TODO: здесь нужно применить сервис статистики
        return null;
    }

    @Override
    public EventFullResponseDto update(Long eventId, EventUpdateDto updateDto) {
        return null;
    }

    @Override
    public List<EventFullResponseDto> getAllFullEvents(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size, HttpServletRequest request) {
        return null;
    }
}
