package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Embeddable
public class Location {
    private Double lat = 0d;
    private Double lon = 0d;
}
