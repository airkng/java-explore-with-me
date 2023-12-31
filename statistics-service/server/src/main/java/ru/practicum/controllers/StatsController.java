package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndPointRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.service.StatsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {
    private final StatsService service;
    private final StatsMapper mapper;

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ViewStatsResponseDto>> get(@RequestParam(value = "start", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @NotNull final LocalDateTime start,
                                                          @RequestParam(value = "end", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @NotNull final LocalDateTime end,
                                                          @RequestParam(value = "uris", required = false) final List<String> uris,
                                                          @RequestParam(value = "unique", required = false, defaultValue = "false") final Boolean unique,
                                                          HttpServletRequest request) {

        ViewStats viewStats = mapper.toViewStats(start, end, uris, unique);
        log.info("Запрос на получение статистики(stats) {} ", viewStats);
        return service.get(viewStats);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EndPointHit> addHit(@RequestBody EndPointRequestDto endPointRequestDto) {
        log.info("Создание новой статистики{}", endPointRequestDto);
        return service.add(endPointRequestDto);
    }

}
