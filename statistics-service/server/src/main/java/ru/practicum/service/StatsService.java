package ru.practicum.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.EndPointHitDto;
import ru.practicum.dto.ViewStatsHitDto;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;

import java.util.List;

public interface StatsService {
    ResponseEntity<List<ViewStatsHitDto>> get(ViewStats viewStats);

    ResponseEntity<EndPointHit> add(EndPointHitDto endPointHitDto);
}
