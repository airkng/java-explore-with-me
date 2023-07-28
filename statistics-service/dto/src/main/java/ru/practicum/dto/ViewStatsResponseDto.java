package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
//Возвращается при запросе GET /stats
public class ViewStatsResponseDto {

    private String app; // ex. ewm-main-service

    private String uri; // ex. /events/1

    private Long hits; // ex. 6
}
