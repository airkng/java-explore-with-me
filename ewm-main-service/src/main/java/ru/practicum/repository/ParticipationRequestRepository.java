package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Participation;
import ru.practicum.model.enums.ParticipationState;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<Participation, Long> {
    List<Participation> findAllByRequesterId(Long userId);

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<Participation> findByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<Participation> findByIdAndRequesterId(Long requestId, Long userId);

    List<Participation> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);

    Long countByEventIdAndStatus(Long id, ParticipationState confirmed);
}
