package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.CompilationUpdateDto;
import ru.practicum.dto.request.CompilationRequestDto;
import ru.practicum.dto.response.CompilationResponseDto;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
@Validated
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto addCompilation(@RequestBody @Valid final CompilationRequestDto compilationRequestDto) {
        log.info("Добавлена подборка событий. Работа контроллера CompilationController");
        return service.add(compilationRequestDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(name = "compId") final Long compId) {
        log.info("Удаление подборки в контроллере CompilationController");
        service.delete(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationResponseDto updateCompilation(@RequestBody @Valid final CompilationUpdateDto compilationUpdateDto,
                                                    @PathVariable(name = "compId") final Long compId) {
        log.info("Изменение подборки. контроллер CompilationController");
        return service.update(compilationUpdateDto, compId);
    }
}
