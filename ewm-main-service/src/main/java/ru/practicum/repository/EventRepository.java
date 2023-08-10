package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Set<Event> findAllByIdIn(Set<Long> events);

    List<Event> findAllByInitiatorId(Long userId, Pageable page);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    boolean existsByCategoryId(Long categoryId);
}
