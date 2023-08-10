package ru.practicum.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class ParticipationStatusUpdateResponseDto {
    private List<ParticipationResponseDto> confirmedRequests;
    private List<ParticipationResponseDto> rejectedRequests;
}
