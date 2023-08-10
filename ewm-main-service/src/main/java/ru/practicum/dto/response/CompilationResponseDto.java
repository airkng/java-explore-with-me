package ru.practicum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.EventShortDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class CompilationResponseDto {
    private List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}
