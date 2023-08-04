package ru.practicum.mappers;

import org.springframework.stereotype.Component;
import ru.practicum.dto.request.CategoryRequestDto;
import ru.practicum.dto.response.CategoryResponseDto;
import ru.practicum.model.Category;

@Component
public class CategoryMapper {

    public CategoryResponseDto toCategoryResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toCategory(CategoryRequestDto categoryRequestDto) {
        return Category.builder()
                .name(categoryRequestDto.getName())
                .build();
    }
}
