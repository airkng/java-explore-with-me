package ru.practicum.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
}
