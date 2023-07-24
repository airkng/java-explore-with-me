package practicum.service;

import org.springframework.http.ResponseEntity;
import practicum.model.ViewStats;
import ru.practicum.dto.EndPointHitDto;
import ru.practicum.dto.ViewStatsHitDto;

import java.util.List;

public interface StatsService {
    List<ViewStatsHitDto> get(ViewStats viewStats);

    ResponseEntity<Object> add(EndPointHitDto endPointHitDto);
}
