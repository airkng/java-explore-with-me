package ru.practicum.controllers.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.response.CategoryResponseDto;
import ru.practicum.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Validated
@Slf4j
public class PubCategoryController {
    private final CategoryService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponseDto> getAllCategories(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero final Integer from,
                                                      @RequestParam(name = "size", defaultValue = "10") @Positive final Integer size) {
        log.info("Получение всех категорий. Контроллер PubCategoryController");
        return service.geAll(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto getCategoryById(@PathVariable(name = "catId") final Long id) {
        log.info("Получение конкретной категории. Контроллер PubCategoryController");
        return service.getById(id);
    }
}
