package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.UserRequestDto;
import ru.practicum.dto.response.UserResponseDto;
import ru.practicum.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserResponseDto> getUsers(@RequestParam(required = false) final List<Long> ids,
                                          @RequestParam(required = false, defaultValue = "0") @PositiveOrZero final Integer from,
                                          @RequestParam(required = false, defaultValue = "10") @Positive final Integer size) {
        log.info("Запрос на получение пользователей в контроллере");
        return service.get(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto addUser(@RequestBody @Valid final UserRequestDto userRequestDto) {
        log.info("Запрос на создание пользователя");
        return service.add(userRequestDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "userId") final Long userId) {
        log.info("запрос на удаление пользователя");
        service.delete(userId);
    }

}
