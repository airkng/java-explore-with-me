package ru.practicum.service;

import ru.practicum.dto.request.CategoryRequestDto;
import ru.practicum.dto.response.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto add(CategoryRequestDto categoryRequestDto);

    void delete(Long id);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto);

    List<CategoryResponseDto> geAll(Integer from, Integer size);

    CategoryResponseDto getById(Long id);
}
