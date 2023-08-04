package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.response.EventRequestResponseDto;
import ru.practicum.exceptions.AlreadyExistsException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.ParticipationRequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.model.enums.EventState;
import ru.practicum.model.enums.ParticipationRequestState;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventRequestServiceImpl implements EventRequestService{
    private final ParticipationRequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    private final ParticipationRequestMapper mapper;

    @Override
    public List<EventRequestResponseDto> getAll(Long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException("user not found");
        }

        List<ParticipationRequest> requests = repository.findAllByRequesterId(userId);
        if (requests.isEmpty()) {
            return List.of();
        }
        return requests.stream()
                .map(mapper::toEventRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestResponseDto add(Long userId, Long eventId) {
        /**
         * нельзя добавить повторный запрос (Ожидается код ошибки 409)
         * инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
         * нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
         * если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
         * если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
         */

        Optional<ParticipationRequest> requestOptional = repository.findByRequesterIdAndEventId(userId, eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));

        if (requestOptional.isPresent()) {
            throw new AlreadyExistsException("Повторное добавление запроса на событие");
        }

        if (requestOptional.get().getEvent().getInitiator().getId().equals(userId)) {
            throw new ConflictException("Пользователь не может забронировать у себя место");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("нельзя отправить заявку на неопубликованное событие");
        }
        if (event.getParticipantLimit() != 0) {
            if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
                throw new ConflictException("Лимит заявок привышен");
            }
        }

        if (!event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return mapper.toEventRequestResponseDto(repository.save(mapper.toParticipationRequest(event, user)));
    }

    @Override
    public EventRequestResponseDto cancel(Long userId, Long requestId) {
        Optional<ParticipationRequest> requestOpt = repository.findByIdAndRequesterId(requestId, userId);
        var request = requestOpt.orElseThrow(() -> new NotFoundException("Request not found"));
        request.setStatus(ParticipationRequestState.CANCELED);
        return mapper.toEventRequestResponseDto(repository.save(request));
    }
}
