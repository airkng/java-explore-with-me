package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EndPointRequestDto;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class StatsMapper {
    public ViewStats toViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return ViewStats.builder()
                .start(start)
                .end(end)
                .uris(uris)
                .unique(unique)
                .build();

    }

    public EndPointHit toEndpointHit(EndPointRequestDto endPointRequestDto) {
        return EndPointHit.builder()
                .app(endPointRequestDto.getApp())
                .ip(endPointRequestDto.getIp())
                .uri(endPointRequestDto.getUri())
                .timestamp(endPointRequestDto.getTimestamp())
                .build();
    }
}
