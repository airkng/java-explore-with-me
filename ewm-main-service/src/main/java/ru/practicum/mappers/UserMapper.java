package ru.practicum.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.dto.UserRequestDto;
import ru.practicum.dto.UserResponseDto;
import ru.practicum.model.User;

@Component
public class UserMapper {
    public User toUser(UserRequestDto userRequestDto) {
        return User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .build();
    }

    public UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
