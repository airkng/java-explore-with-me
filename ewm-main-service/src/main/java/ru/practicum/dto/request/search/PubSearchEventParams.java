package ru.practicum.dto.request.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.model.enums.EventSort;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PubSearchEventParams {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private EventSort sort;
    private Integer from;
    private Integer size;

    public boolean checkDate() {
        if (this.rangeStart != null && this.rangeEnd != null && this.rangeStart.isAfter(this.rangeEnd)) {
            throw new BadRequestException("Date incorrect!");
        }
        return true;
    }
}
