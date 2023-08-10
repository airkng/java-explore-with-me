package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.UserRequestDto;
import ru.practicum.dto.response.UserResponseDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.UniqueViolationException;
import ru.practicum.mappers.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;
import ru.practicum.utils.PageBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class
UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserResponseDto> get(List<Long> ids, Integer from, Integer size) {
        log.info("Получение пользователей в сервисная логика");
        Pageable page = PageBuilder.create(from, size);
        Page<User> userPage;
        if (ids == null || ids.isEmpty()) {
            userPage = repository.findAll(page);
        } else {
            userPage = repository.findAllByIdIn(ids, page);
        }
        if (userPage.isEmpty()) {
            return List.of();
        }
        return userPage.stream()
                .map(mapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto add(UserRequestDto userRequestDto) {
        log.info("Добавление пользователя в сервисе");
        var user = mapper.toUser(userRequestDto);
        try {
            return mapper.toUserResponseDto(repository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException(String.format("User with email %s already exists", user.getEmail()));
        }

    }

    @Override
    public void delete(Long userId) {
        if (repository.existsById(userId)) {
            log.info("Удаление существующего пользователя в сервисе");
            repository.deleteById(userId);
        } else {
            log.warn("Попытка удаления несуществующего пользователя в сервисе. Выброс исключения");
            throw new NotFoundException("User with id = " + userId + " not found");
        }
    }
}
