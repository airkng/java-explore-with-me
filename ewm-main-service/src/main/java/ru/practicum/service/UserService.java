package ru.practicum.service;

import org.springframework.data.domain.Page;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> get(List<Long> ids, Integer from, Integer size);

    UserResponseDto add(UserRequestDto userRequestDto);

    void delete(Long userId);
}
