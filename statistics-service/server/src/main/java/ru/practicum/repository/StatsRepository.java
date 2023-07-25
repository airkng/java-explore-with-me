package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndPointHit;
import ru.practicum.dto.ViewStatsHitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndPointHit, Integer> {

    @Query("SELECT new ru.practicum.dto.ViewStatsHitDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            " FROM EndPointHit AS h " +
            " WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            " GROUP BY h.uri, h.app " +
            " ORDER BY COUNT(DISTINCT h.ip) desc")
    List<ViewStatsHitDto> findAllByTimeAndUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsHitDto(h.app, h.uri, COUNT(h.ip)) " +
            " FROM EndPointHit AS h WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            " GROUP BY h.uri, h.app " +
            " ORDER BY COUNT(h.ip) desc")
    List<ViewStatsHitDto> findAllByTime(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsHitDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            " FROM EndPointHit AS h " +
            " WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            " AND h.uri IN ?3" +
            " GROUP BY h.uri, h.app " +
            " ORDER BY COUNT(DISTINCT h.ip) desc")
    List<ViewStatsHitDto> findByUrisAndTimeAndUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.dto.ViewStatsHitDto(h.app, h.uri, COUNT(h.ip)) " +
            " FROM EndPointHit AS h" +
            " WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            " AND h.uri IN ?3" +
            " GROUP BY h.uri, h.app " +
            " ORDER BY COUNT(h.ip) desc")
    List<ViewStatsHitDto> findByUrisAndTime(LocalDateTime start, LocalDateTime end, List<String> uris);
}
