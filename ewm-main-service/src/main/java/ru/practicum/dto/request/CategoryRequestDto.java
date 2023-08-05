package ru.practicum.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class CategoryRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
