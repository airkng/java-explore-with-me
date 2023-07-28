package ru.practicum.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.EndPointRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;

import java.util.List;

public interface StatsService {
    ResponseEntity<List<ViewStatsResponseDto>> get(ViewStats viewStats);

    ResponseEntity<EndPointHit> add(EndPointRequestDto endPointRequestDto);
}
