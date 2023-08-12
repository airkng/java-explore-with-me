package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByIdAndAuthorId(Long commentId, Long userId);

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findAllByEventId(Long eventId, Pageable page);

    List<Comment> findAllByAuthorId(Long userId, Pageable page);

    List<Comment> findAllByTextContainingIgnoreCase(String text, Pageable page);
}
