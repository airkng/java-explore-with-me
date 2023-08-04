package ru.practicum.service;

import ru.practicum.dto.request.UserRequestDto;
import ru.practicum.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> get(List<Long> ids, Integer from, Integer size);

    UserResponseDto add(UserRequestDto userRequestDto);

    void delete(Long userId);
}
