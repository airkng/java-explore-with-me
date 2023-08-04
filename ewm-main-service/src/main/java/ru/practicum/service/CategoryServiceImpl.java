package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.CategoryRequestDto;
import ru.practicum.dto.response.CategoryResponseDto;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.UniqueViolationException;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryResponseDto add(CategoryRequestDto categoryRequestDto) {
        Category category = mapper.toCategory(categoryRequestDto);
        try {
            return mapper.toCategoryResponseDto(repository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("name is already exists");
        }
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new NotFoundException("Category with id = " + id + " not found");
        }
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto) {
        var oldCategory = repository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        if (oldCategory.getName().equals(categoryRequestDto.getName())) {
            throw new UniqueViolationException("name of category already exists");
        }
        //возможно добавить на проверку есть ли события с этим айдишником
        return mapper.toCategoryResponseDto(repository.save(mapper.toCategory(categoryRequestDto)));
    }

    @Override
    public List<CategoryResponseDto> geAll(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        List<Category> categories = repository.findAll(page).getContent();
        if (categories.isEmpty()) {
            return List.of();
        }
        return categories.stream()
                .map(mapper::toCategoryResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return mapper.toCategoryResponseDto(repository.findById(id)
                .orElseThrow(() -> {
                  throw new NotFoundException("Category not found");
                }));
    }
}
