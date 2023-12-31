package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndPointRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.EndPointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    private final StatsMapper mapper;

    @Override
    public ResponseEntity<List<ViewStatsResponseDto>> get(ViewStats viewStatsHitDto) {
        validateHitDateTime(viewStatsHitDto);
        List<ViewStatsResponseDto> views;
        if (viewStatsHitDto.getUris() == null || viewStatsHitDto.getUris().isEmpty()) {
            if (viewStatsHitDto.getUnique()) {
                views = repository.findAllByTimeAndUnique(viewStatsHitDto.getStart(), viewStatsHitDto.getEnd());
            } else {
                views = repository.findAllByTime(viewStatsHitDto.getStart(), viewStatsHitDto.getEnd());
            }

        } else {
            List<String> uris = viewStatsHitDto.getUris().stream()
                    .map(str -> str.replaceAll("\\[", "").replaceAll("\\]", ""))
                    .collect(Collectors.toList());
            if (viewStatsHitDto.getUnique()) {
                views = repository.findByUrisAndTimeAndUnique(viewStatsHitDto.getStart(), viewStatsHitDto.getEnd(), uris);
            } else {
                views = repository.findByUrisAndTime(viewStatsHitDto.getStart(), viewStatsHitDto.getEnd(), uris);
            }

        }
        return ResponseEntity.ok().body(views);
    }

    private void validateHitDateTime(ViewStats viewStatsHitDto) {
        if (viewStatsHitDto.getStart().isAfter(viewStatsHitDto.getEnd())) {
            throw new BadRequestException("Start datetime is after then end datetime");
        }
    }

    @Override
    public ResponseEntity<EndPointHit> add(EndPointRequestDto endPointRequestDto) {
        var endpointHit = mapper.toEndpointHit(endPointRequestDto);
        repository.save(endpointHit);
        return ResponseEntity.created(URI.create("")).body(endpointHit);
    }
}
