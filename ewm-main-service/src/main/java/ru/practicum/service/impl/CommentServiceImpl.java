package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.CommentRequestDto;
import ru.practicum.dto.response.CommentResponseDto;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.CommentService;
import ru.practicum.utils.PageBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentMapper mapper;
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentResponseDto> getAllFromEvent(final Long eventId, final Integer from, final Integer size) {
        Pageable page = PageBuilder.create(from, size);
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found");
        }
        return repository.findAllByEventId(eventId, page).stream()
                .map(mapper::toCommentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDto> getAllByUser(final Long userId, final Integer from, final Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        Pageable page = PageBuilder.create(from, size);
        return repository.findAllByAuthorId(userId, page).stream()
                .map(mapper::toCommentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDto> searchByText(final String text, final Integer from, final Integer size) {
        Pageable page = PageBuilder.create(from, size);
        repository.findAllByTextContainingIgnoreCase(text, page);
        if (text.isBlank()) {
            return repository.findAll(page).getContent()
                    .stream()
                    .map(mapper::toCommentResponseDto)
                    .collect(Collectors.toList());
        }
        return repository.findAllByTextContainingIgnoreCase(text, page)
                .stream()
                .map(mapper::toCommentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto add(final Long userId, final Long eventId, final CommentRequestDto commentDto) {
        log.trace("Поиск и получение пользователя из БД с id = {} ", userId);
        final User author = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        log.trace("Поиск и получение события из БД с id = {} ", eventId);
        final Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        final Comment comment = mapper.toComment(commentDto);
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());
        log.trace("Сохранение в БД комментария {}", comment);
        return mapper.toCommentResponseDto(repository.save(comment));
    }

    @Override
    public CommentResponseDto update(final Long userId, final Long commentId, final CommentRequestDto commentDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        if (!repository.existsById(commentId)) {
            throw new NotFoundException("Comment not found");
        }
        final Comment oldComment = repository.findByIdAndAuthorId(commentId, userId).orElseThrow(() -> new ConflictException("You are not author of this comment!"));
        log.debug("Text update comment: '{}'", commentDto);
        if (commentDto.getText() != null && !commentDto.getText().isBlank()) {
            oldComment.setText(commentDto.getText());
        } else {
            return mapper.toCommentResponseDto(oldComment);
        }
        return mapper.toCommentResponseDto(repository.save(oldComment));
    }

    @Override
    public void deleteByAdmin(final Long commentId) {
        repository.deleteById(commentId);
        log.info("Comment with id = {} was deleted by Admin");
    }

    @Override
    public void delete(final Long userId, final Long commentId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("user not found");
        }
        if (!repository.existsByIdAndAuthorId(commentId, userId)) {
            throw new NotFoundException("Comment not found");
        }
        repository.deleteById(commentId);
        log.info("Comment with id {} удален из бд", commentId);
    }
}
