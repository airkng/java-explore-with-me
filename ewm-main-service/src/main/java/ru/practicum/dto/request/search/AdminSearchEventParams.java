package ru.practicum.dto.request.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class AdminSearchEventParams {
    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Integer from;
    private Integer size;

    public boolean checkDate() {
        if (this.rangeStart != null && this.rangeEnd != null && this.rangeStart.isAfter(this.rangeEnd)) {
            throw new BadRequestException("Date incorrect!");
        }
        return true;
    }
}
