package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.controllers.priv.PrivEventController;
import ru.practicum.dto.request.ParticipationStatusUpdateRequestDto;
import ru.practicum.dto.response.ParticipationResponseDto;
import ru.practicum.dto.response.ParticipationStatusUpdateResponseDto;
import ru.practicum.exceptions.AlreadyExistsException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.ParticipationMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Participation;
import ru.practicum.model.User;
import ru.practicum.model.enums.EventState;
import ru.practicum.model.enums.ParticipationState;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventRequestServiceImpl implements EventRequestService {
    private final ParticipationRequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private final ParticipationMapper mapper;

    /**
     * Возвращает все запросы на события текущего пользователя {@link Participation}
     * <p>Контроллер: {@link ru.practicum.controllers.priv.PrivEventRequestController}</p>
     * <p><b>Эндпоинт: /users/{userId}/requests</b> </p>
     *
     * @param userId уникальный идентификатор пользователя {@link User}
     * @return {@link ParticipationResponseDto}
     */

    @Override
    @Transactional
    public List<ParticipationResponseDto> getAll(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("user not found");
        }
        List<Participation> requests = repository.findAllByRequesterId(userId);
        if (requests.isEmpty()) {
            log.info("События не найдены. Возвращается пустой список");
            return List.of();
        }
        log.info("Происходит возвращение листа запросов");
        return requests.stream()
                .map(mapper::toParticipationResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Добавляет в БД сущность {@link Participation Participation.class}. если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
     * <p><b>Контроллер: {@link ru.practicum.controllers.priv.PrivEventRequestController PrivEventController.class}</b></p>
     * <p><b>Эндпоинт: /users/{userId}/requests?eventId=... </b></p>
     *
     * @param userId  уникальный идентификатор {@link User User.class} который хочет забронировать
     * @param eventId уникальный индентификатор {@link Event Event.class} события в котором хотят участвовать
     * @return {@link ParticipationResponseDto ParticipationResponseDto.class}
     * @throws AlreadyExistsException при повторном запросе
     * @throws NotFoundException      если не существует события/пользователя
     * @throws ConflictException      если инициатор события бронирует свое событие / событие неопубликованно(EventState != PUBLISHED)
     */
    @Override
    @Transactional
    public ParticipationResponseDto add(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        Optional<Participation> requestOptional = repository.findByRequesterIdAndEventId(userId, eventId);

        if (requestOptional.isPresent()) {
            throw new AlreadyExistsException("Повторное добавление запроса на событие");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("нельзя отправить заявку на неопубликованное событие");
        }
        if (event.getParticipantLimit() != 0) {
            if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new ConflictException("Лимит заявок привышен");
            }
        }
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("You cant request by yourself");
        }
        Participation participation = mapper.toParticipation(event, user);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participation.setStatus(ParticipationState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        log.info("Сохранение в БД запроса на участие в событии");
        return mapper.toParticipationResponseDto(repository.save(participation));
    }

    /**
     * Отклоняет в БД запрос на участие в событии {@link Event Event.class}
     * Присваивает запросу {@link Participation Participation.class} статус {@link ParticipationState ParticipationState.class} <b>CANCELED</b>
     * <p><b>Контроллер: {@link ru.practicum.controllers.priv.PrivEventRequestController PrivEventController.class}</b></p>
     * <p><b>Эндпоинт: /users/{userId}/{requestId}/cancel </b></p>
     *
     * @param userId    уникальный идентификатор пользователя (id) {@link User User.class}
     * @param requestId уникальный идентификатор запроса (id) {@link Participation Participation.class}
     * @return {@link ParticipationResponseDto ParticipationResponseDto.class}
     * @throws NotFoundException если запрос на участие с пользователем не был найден
     */
    @Override
    public ParticipationResponseDto cancel(Long userId, Long requestId) {
        var request = repository.findByIdAndRequesterId(requestId, userId).orElseThrow(() -> new NotFoundException("Request not found"));
        if (request.getStatus().equals(ParticipationState.CONFIRMED)) {
            Event event = request.getEvent();
            log.info("Удаление ранее подтвержденного запроса из {}. Сохранение в БД", event);
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        request.setStatus(ParticipationState.CANCELED);
        return mapper.toParticipationResponseDto(repository.save(request));
    }

    /**
     * Получение информации о запросах(заявках) на участие в событии текущего пользователя.
     * Запрашивает в БД все заявки, полученные от других пользователей на событие по параметрам сущности Event переменных EventId EventInitiatorId
     * <p><b>Эндпоинт: GET /users/{userId}/events/{eventId}/requests</b></p>
     * <p><b>Контроллер: {@link PrivEventController PrivEventController.class}</b></p>
     *
     * @param userId  id владельца события Event initiator
     * @param eventId id события
     * @return List {@link ParticipationResponseDto ParticipationResponseDto.class} список заявок на участие
     */
    @Override
    public List<ParticipationResponseDto> getAllUserRequests(Long userId, Long eventId) {
        List<Participation> requests = repository.findAllByEventIdAndEventInitiatorId(eventId, userId);
        if (requests.isEmpty()) {
            return List.of();
        }
        return requests.stream()
                .map(mapper::toParticipationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationStatusUpdateResponseDto updateRequestStatus(Long eventId, ParticipationStatusUpdateRequestDto requestDto, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("User is not initiator");
        }
        Long max = event.getParticipantLimit();
        Long current = event.getConfirmedRequests();

        if (max.longValue() == current.longValue() && max != 0) {
            throw new ConflictException("лимит исчерпан");
        }

        List<Participation> requests = repository.findAllById(requestDto.getRequestIds());

        if (requests.stream().anyMatch(request -> !request.getStatus().equals(ParticipationState.PENDING))) {
            throw new ConflictException("request already has result");
        }

        ParticipationStatusUpdateResponseDto result = ParticipationStatusUpdateResponseDto.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();

        if (requestDto.getStatus().equals(ParticipationState.CONFIRMED)) {
            for (Participation request : requests) {
                if (max.longValue() != current.longValue()) {
                    current++;
                    request.setStatus(ParticipationState.CONFIRMED);
                    result.getConfirmedRequests().add(mapper.toParticipationResponseDto(request));
                } else {
                    request.setStatus(ParticipationState.REJECTED);
                    result.getRejectedRequests().add(mapper.toParticipationResponseDto(request));
                }
            }
        } else if (requestDto.getStatus().equals(ParticipationState.REJECTED)) {
            for (Participation request : requests) {
                if (request.getStatus().equals(ParticipationState.CONFIRMED)) {
                    current--;
                }
                request.setStatus(ParticipationState.REJECTED);
                result.getRejectedRequests().add(mapper.toParticipationResponseDto(request));

            }
        }
        if (!requests.isEmpty()) {
            repository.saveAll(requests);
        }
        event.setConfirmedRequests(current);
        eventRepository.save(event);
        return result;

    }
}
