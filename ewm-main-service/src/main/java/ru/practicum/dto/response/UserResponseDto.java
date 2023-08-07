package ru.practicum.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserResponseDto {
    private String email;
    private Long id;
    private String name;
}
