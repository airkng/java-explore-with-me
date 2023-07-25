package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndPointHitDto;
import ru.practicum.dto.ViewStatsHitDto;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    private final StatsMapper mapper;

    @Override
    public ResponseEntity<List<ViewStatsHitDto>> get(ViewStats viewStatsHitDto) {
        checkDateTime(viewStatsHitDto);
        List<ViewStatsHitDto> views;
        if (viewStatsHitDto.getUris() == null || viewStatsHitDto.getUris().isEmpty()) {
            //
            if (viewStatsHitDto.getUnique()) {
                views = repository.findAllByTimeAndUnique(viewStatsHitDto.getStart(), viewStatsHitDto.getEnd());
            } else {
                views = repository.findAllByTime(viewStatsHitDto.getStart(), viewStatsHitDto.getEnd());
            }

        } else {
            if (viewStatsHitDto.getUnique()) {
                views = repository.findByUrisAndTimeAndUnique(viewStatsHitDto.getStart(), viewStatsHitDto.getEnd(), viewStatsHitDto.getUris());
            } else {
                views = repository.findByUrisAndTime(viewStatsHitDto.getStart(), viewStatsHitDto.getEnd(), viewStatsHitDto.getUris());
            }

        }
        return ResponseEntity.ok().body(views);
    }

    private void checkDateTime(ViewStats viewStatsHitDto) {
        if (viewStatsHitDto.getStart().isAfter(viewStatsHitDto.getEnd())) {
            throw new IllegalArgumentException("Start datetime is after then end datetime");
        }
    }

    @Override
    public ResponseEntity<EndPointHit> add(EndPointHitDto endPointHitDto) {
        var endpointHit = mapper.toEndpointHit(endPointHitDto);
        repository.save(endpointHit);
        return ResponseEntity.created(URI.create("")).body(endpointHit);
    }
}
