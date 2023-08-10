package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.CategoryRequestDto;
import ru.practicum.dto.response.CategoryResponseDto;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto addCategory(@RequestBody @Valid final CategoryRequestDto categoryRequestDto) {
        log.info("Добавление категории в контроллер");
        return service.add(categoryRequestDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = "catId") final Long id) {
        log.info("Удаление категории в контроллере");
        service.delete(id);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto updateCategory(@PathVariable(name = "catId") final Long id,
                                              @RequestBody @Valid final CategoryRequestDto categoryRequestDto) {
        log.info("изменение категории в контроллеер");
        return service.update(id, categoryRequestDto);
    }

}
