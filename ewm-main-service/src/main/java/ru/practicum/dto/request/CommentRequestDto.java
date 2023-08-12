package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class CommentRequestDto {
    @NotBlank(message = "comment cannot blank")
    @Size(min = 1, max = 7000)
    private String text;
}

