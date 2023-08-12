package ru.practicum.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.request.CommentRequestDto;
import ru.practicum.dto.response.CommentResponseDto;
import ru.practicum.model.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    public Comment toComment(CommentRequestDto input) {
        return Comment.builder()
                .text(input.getText()).build();
    }

    public CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .event(eventMapper.toEventShortDto(comment.getEvent()))
                .author(userMapper.toShortUser(comment.getAuthor()))
                .createdOn(comment.getCreatedOn())
                .text(comment.getText()).build();
    }
}
