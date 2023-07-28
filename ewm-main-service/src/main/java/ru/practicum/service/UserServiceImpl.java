package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CompositeIterator;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserResponseDto> get(List<Long> ids, Integer from, Integer size) {
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
        var user = mapper.toUser(userRequestDto);
        return mapper.toUserResponseDto(repository.save(user));

    }

    @Override
    public void delete(Long userId) {
        if (repository.existsById(userId)) {
            repository.deleteById(userId);
        } else {
          throw new NotFoundException("User with id = " + userId + " not found");
        }
    }
}
