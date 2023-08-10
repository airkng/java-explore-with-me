package ru.practicum.controllers.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.response.CompilationResponseDto;
import ru.practicum.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@Validated
@Slf4j
@RequiredArgsConstructor
public class PubCompilationController {
    private final CompilationService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationResponseDto> getAllCompilations(@RequestParam(defaultValue = "false", required = false) final Boolean pinned,
                                                           @RequestParam(defaultValue = "0", required = false) @PositiveOrZero final Integer from,
                                                           @RequestParam(defaultValue = "10", required = false) @Positive final Integer size) {
        log.info("Запрос на получение всех подборок в контроллере /compilations");
        return service.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationResponseDto getCompilationById(@PathVariable(name = "compId") final Long id) {
        log.info("запрос на получение подборки по Id в контроллере /compilations/compId");
        return service.getById(id);
    }
}
