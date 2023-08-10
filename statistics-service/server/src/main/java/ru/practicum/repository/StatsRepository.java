package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.model.EndPointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndPointHit, Integer> {

    @Query("SELECT new ru.practicum.dto.ViewStatsResponseDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            " FROM EndPointHit AS h " +
            " WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            " GROUP BY h.uri, h.app " +
            " ORDER BY COUNT(DISTINCT h.ip) desc")
    List<ViewStatsResponseDto> findAllByTimeAndUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsResponseDto(h.app, h.uri, COUNT(h.ip)) " +
            " FROM EndPointHit AS h WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            " GROUP BY h.uri, h.app " +
            " ORDER BY COUNT(h.ip) desc")
    List<ViewStatsResponseDto> findAllByTime(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsResponseDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            " FROM EndPointHit AS h " +
            " WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            " AND h.uri IN ?3" +
            " GROUP BY h.uri, h.app " +
            " ORDER BY COUNT(DISTINCT h.ip) desc")
    List<ViewStatsResponseDto> findByUrisAndTimeAndUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.dto.ViewStatsResponseDto(h.app, h.uri, COUNT(h.ip)) " +
            " FROM EndPointHit AS h" +
            " WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            " AND h.uri IN ?3" +
            " GROUP BY h.uri, h.app " +
            " ORDER BY COUNT(h.ip) desc")
    List<ViewStatsResponseDto> findByUrisAndTime(LocalDateTime start, LocalDateTime end, List<String> uris);
}
