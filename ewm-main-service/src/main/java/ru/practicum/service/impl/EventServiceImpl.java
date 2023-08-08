package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.controllers.priv.PrivEventController;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.request.EventRequestDto;
import ru.practicum.dto.request.EventUpdateDto;
import ru.practicum.dto.request.search.AdminSearchEventParams;
import ru.practicum.dto.request.search.PubSearchEventParams;
import ru.practicum.dto.response.EventFullResponseDto;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.model.enums.EventState;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventService;
import ru.practicum.utils.PageBuilder;
import ru.practicum.utils.StatsServiceTemplate;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository participationRepository;
    private final EventMapper mapper;
    private final EntityManager manager;
    private final StatsServiceTemplate statsTemplate;

    @Override
    @Transactional
    public EventFullResponseDto add(final EventRequestDto eventRequestDto, final Long userId) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        Event event = mapper.toEvent(eventRequestDto);
        Category category = categoryRepository.findById(eventRequestDto.getCategory()).orElseThrow(() -> new NotFoundException("category not found"));
        event.setInitiator(initiator);
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(category);
        event.setState(EventState.PENDING);
        event.setViews(0L);
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        }
        return mapper.toEventResponseDto(repository.save(event));
    }

    /**
     * Получение полного события {@link Event Event.class} добавленного текущим пользователем {@link User User.class}
     * <p><b>Эндпоинт: /users/{userId}/events/{eventId}</b></p>
     * <p><b>Контроллер: {@link PrivEventController PrivEventController.class}</b></p>
     *
     * @param userId  уникальный идентификатор пользователя
     * @param eventId уникальный идентификатор события
     * @return EventFullResponseDto
     * @throws NotFoundException пользователь не найден / userId не совпадает с Initiator id
     */
    @Override
    public EventFullResponseDto getEventByIdAndInitiatorId(final Long userId, final Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("user not found");
        }
        Event event = repository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new NotFoundException("event not found"));
        setViews(event);
        return mapper.toEventResponseDto(event);
    }

    /**
     * Получение событий {@link Event Event.class} добавленных текущим пользователем {@link User User.class}
     * <p><b>Эндпоинт: /users/{userId}/events</b></p>
     * <p><b>Контроллер: {@link PrivEventController PrivEventController.class}</b></p>
     *
     * @param userId id владельца события Event
     * @param from   для постраничной загрузки
     * @param size   для постраничной загрузки
     * @return List {@link EventShortDto EventShortDto.class}
     * @throws NotFoundException если пользователь не найден
     */
    @Override
    public List<EventShortDto> getAllUserId(final Long userId, final Integer from, final Integer size) {
        Pageable page = PageBuilder.create(from, size);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("user not found");
        }
        List<Event> events = repository.findAllByInitiatorId(userId, page);
        if (events.isEmpty()) {
            return List.of();
        }
        return events.stream()
                .map(this::setViews)
                .map(mapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullResponseDto updateByUser(final Long eventId, EventUpdateDto updateDto, final Long userId) {
        Event oldEvent = repository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new NotFoundException("event not found"));
        if (oldEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("you cant change published event");
        }
        update(oldEvent, updateDto);
        if (updateDto.getStateAction() != null) {
            switch (updateDto.getStateAction()) {
                case SEND_TO_REVIEW:
                    oldEvent.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    oldEvent.setState(EventState.CANCELED);
                    break;
                default:
                    throw new ConflictException("U r not admin!");
            }
        }
        return mapper.toEventResponseDto(repository.save(oldEvent));
    }

    private void update(Event event, EventUpdateDto updateDto) {
        if (updateDto.getAnnotation() != null) {
            event.setAnnotation(updateDto.getAnnotation());
        }
        if (updateDto.getTitle() != null) {
            event.setTitle(updateDto.getTitle());
        }
        if (updateDto.getDescription() != null) {
            event.setDescription(updateDto.getDescription());
        }
        if (updateDto.getCategory() != null) {
            Category cat = categoryRepository.findById(updateDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Cat not found"));
            event.setCategory(cat);
        }
        if (updateDto.getLocation() != null) {
            event.setLocation(updateDto.getLocation());
        }
        if (updateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateDto.getParticipantLimit());
        }
        if (updateDto.getEventDate() != null) {
            if (updateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Date incorrect!");
            }
            event.setEventDate(updateDto.getEventDate());
        }
        if (updateDto.getRequestModeration() != null) {
            event.setRequestModeration(updateDto.getRequestModeration());
        }
        if (updateDto.getPaid() != null) {
            event.setPaid(updateDto.getPaid());
        }
    }

    @Override
    public EventFullResponseDto updateByAdmin(Long eventId, EventUpdateDto updateDto) {
        Event oldEvent = repository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!oldEvent.getState().equals(EventState.PENDING)) {
            throw new ConflictException("you cant change event");
        }
        update(oldEvent, updateDto);
        if (updateDto.getStateAction() != null) {
            switch (updateDto.getStateAction()) {
                case REJECT_EVENT:
                    oldEvent.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    oldEvent.setState(EventState.PUBLISHED);
                    oldEvent.setPublishedOn(LocalDateTime.now());
                    break;
                default:
                    throw new ConflictException("you not owner!");
            }
        }
        return mapper.toEventResponseDto(repository.save(oldEvent));
    }

    @Override
    public List<EventShortDto> getAllPublished(PubSearchEventParams params, HttpServletRequest request) {
        params.checkDate();
        List<Event> events;
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate predicate = builder.conjunction();
        Order order = builder.asc(root);
        predicate = builder.and(predicate, root.get("state").in(EventState.PUBLISHED));
        if (params.getText() != null && !params.getText().isEmpty()) {
            Predicate annotation = builder.like(builder.lower(root.get("annotation")), "%" + params.getText().toLowerCase() + "%");
            Predicate description = builder.like(builder.lower(root.get("description")), "%" + params.getText().toLowerCase() + "%");
            predicate = builder.and(predicate, builder.or(annotation, description));
        }
        if (params.getCategories() != null) {
            predicate = builder.and(predicate, root.get("category").get("id").in(params.getCategories()));
        }
        if (params.getPaid() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("paid"), params.getPaid()));
        }
        if (params.getRangeStart() != null) {
            predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), params.getRangeStart()));
        }
        if (params.getRangeEnd() != null) {
            predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), params.getRangeEnd()));
        }
        if (params.getSort() != null) {
            switch (params.getSort()) {
                case EVENT_DATE:
                    order = builder.asc(root.get("eventDate"));
                    break;
                case VIEWS:
                    order = builder.asc(root.get("views"));
                    break;
            }

        }
        events = manager.createQuery(query.select(root)
                        .where(predicate).orderBy(order))
                .setFirstResult(params.getFrom())
                .setMaxResults(params.getSize())
                .getResultList();

        List<Long> ids = events.stream().map(Event::getId).collect(Collectors.toList());
        statsTemplate.sendHits(request, ids);
        if (params.getOnlyAvailable()) {
            return events.stream()
                    .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit())
                    .map(this::setViews)
                    .map(mapper::toEventShortDto)
                    .collect(Collectors.toList());
        }

        return events.stream()
                .map(this::setViews)
                .map(mapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullResponseDto getPublishedById(Long id, HttpServletRequest request) {
        var event = repository.findById(id).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("event is not published");
        }
        statsTemplate.sendHit(request);
        setViews(event);
        return mapper.toEventResponseDto(event);
    }


    @Override
    public List<EventFullResponseDto> getAllFullEvents(AdminSearchEventParams params) {
        params.checkDate();
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate predicate = builder.conjunction();
        Order order = builder.desc(root);
        if (params.getUsers() != null && !params.getUsers().isEmpty()) {
            predicate = builder.and(predicate, root.get("initiator").get("id").in(params.getUsers()));
        }
        if (params.getStates() != null && !params.getStates().isEmpty()) {
            predicate = builder.and(predicate, root.get("state").in(params.getStates()));
        }
        if (params.getCategories() != null) {
            predicate = builder.and(predicate, root.get("category").get("id").in(params.getCategories()));
        }
        if (params.getRangeStart() != null) {
            predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), params.getRangeStart()));
        }
        if (params.getRangeEnd() != null) {
            predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), params.getRangeEnd()));
        }

        return manager.createQuery(query.select(root)
                        .where(predicate).orderBy(order))
                .setFirstResult(params.getFrom())
                .setMaxResults(params.getSize())
                .getResultList()
                .stream()
                .map(this::setViews)
                .map(mapper::toEventResponseDto)
                .collect(Collectors.toList());
    }

    private Event setViews(Event event) {
        Long views = statsTemplate.getStats(event);
        event.setViews(views);
        return event;
    }

}
