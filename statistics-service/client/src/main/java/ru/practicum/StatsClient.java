package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndPointHitDto;

import java.util.List;
import java.util.Map;

public class StatsClient extends BaseClient {
    private static final String POST_PATH = "/hit";
    private static final String GET_PATH = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String statsUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(statsUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<Object> addStat(EndPointHitDto endPointHitDto) {
        return post(POST_PATH, endPointHitDto);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );

        return get(GET_PATH, params);
    }
}
