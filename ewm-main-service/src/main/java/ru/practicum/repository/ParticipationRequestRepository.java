package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(Long userId);

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<ParticipationRequest> findByRequesterIdAndEventId(Long userId, Long eventId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long requestId, Long userId);
}
