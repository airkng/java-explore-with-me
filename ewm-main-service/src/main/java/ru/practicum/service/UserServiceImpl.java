package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.UserRequestDto;
import ru.practicum.dto.response.UserResponseDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.UniqueViolationException;
import ru.practicum.mappers.UserMapper;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserResponseDto> get(List<Long> ids, Integer from, Integer size) {
        log.info("Получение пользователей в сервисная логика");
        Pageable page = PageRequest.of(from / size, size, Sort.by("id").ascending());
        List<UserResponseDto> userResponseDtoPage;
        if (ids == null || ids.isEmpty()) {
            userResponseDtoPage = repository.findAll(page)
                    .stream()
                    .map(mapper::toUserResponseDto)
                    .collect(Collectors.toList());
        } else {
            var userPage = repository.findAllByIdIn(ids, page);
            userResponseDtoPage = userPage.stream()
                    .map(mapper::toUserResponseDto)
                    .collect(Collectors.toList());
        }
        return userResponseDtoPage;
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
