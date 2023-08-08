package ru.practicum.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.StatsClient;
import ru.practicum.dto.EndPointRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsServiceTemplate {
    private final StatsClient client;
    @Value("${ewm.service.name}")
    private String serviceName;

    public void sendHits(HttpServletRequest request, List<Long> ids) {
        for (Long eventId : ids) {
            client.addStat(EndPointRequestDto.builder()
                    .app(serviceName)
                    .ip(request.getRemoteAddr())
                    .uri("/events/" + eventId)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        client.addStat(EndPointRequestDto.builder()
                .app(serviceName)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
    }

    public void sendHit(HttpServletRequest request) {
        client.addStat(EndPointRequestDto.builder()
                .app(serviceName)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build());
    }

    public Long getStats(Event event) {
        try {
            List<String> uris = new ArrayList<>();
            uris.add("/events/" + event.getId());
            ResponseEntity<Object> stat = client.getStats("1900-01-01 00:00:00",
                    "2100-01-01 00:00:00", uris, false);

            ObjectMapper mapper = new ObjectMapper();
            List<ViewStatsResponseDto> statsDto = Arrays.asList(mapper.readValue(mapper.writeValueAsString(stat.getBody()),
                    ViewStatsResponseDto[].class));
            if (statsDto.isEmpty()) {
                return 0L;
            }
            return statsDto.size() + event.getViews();
        } catch (IOException exception) {
            throw new ClassCastException(exception.getMessage());
        }
    }
}
