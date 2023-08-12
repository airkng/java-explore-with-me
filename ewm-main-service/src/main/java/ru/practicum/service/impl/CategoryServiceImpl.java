package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.controllers.pub.PublicCategoryController;
import ru.practicum.dto.request.CategoryRequestDto;
import ru.practicum.dto.response.CategoryResponseDto;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.UniqueViolationException;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CategoryService;
import ru.practicum.utils.PageBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;
    private final CategoryMapper mapper;

    /**
     * Добавляет в БД новую сущность {@link Category Category.class}
     * <p>{@link ru.practicum.controllers.admin.AdminCategoryController AdminCategoryController.class} - <b>основной контроллер</b></b></p>
     * <h3>POST /admin/categories <b> - эндпоинт </b></h3>
     *
     * @param categoryRequestDto {@link CategoryRequestDto CategoryRequestDto.class} dto для создания сущности
     * @return {@link CategoryResponseDto CategoryResponseDto.class} dto возвращаемой сущности
     * @throws UniqueViolationException если параметр name сущности уже существует
     */
    @Override
    public CategoryResponseDto add(final CategoryRequestDto categoryRequestDto) {
        Category category = mapper.toCategory(categoryRequestDto);
        try {
            log.trace("Добавление в БД новой категории");
            return mapper.toCategoryResponseDto(repository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new UniqueViolationException("name is already exists");
        }
    }

    /**
     * Удаляет объект {@link Category Category.class} если он существует
     * <p>{@link ru.practicum.controllers.admin.AdminCategoryController AdminCategoryController.class} - <b>основной контроллер</b></b></p>
     * <h3>DELETE /admin/categories <b> - эндпоинт </b></h3>
     *
     * @param id id сущности
     * @throws NotFoundException                         если сущности с id не существует
     * @throws ru.practicum.exceptions.ConflictException если {@link Event Event.class} c Category id существует
     */
    @Override
    public void delete(final Long id) {
        log.trace("Проверка на существование категории");
        if (repository.existsById(id)) {
            log.trace("проверка существуют ли Event с такой категорией");
            if (eventRepository.existsByCategoryId(id)) {
                throw new ConflictException("Events with this category id was found");
            }
            repository.deleteById(id);
            log.info("категория удалена");
        } else {
            throw new NotFoundException("Category with id = " + id + " not found");
        }
    }

    /**
     * Заменяет в БД сущность {@link Category Category.class}
     * <p>{@link ru.practicum.controllers.admin.AdminCategoryController AdminCategoryController.class} - <b>основной контроллер</b></b></p>
     * <h3>PATCH /admin/categories <b> - эндпоинт </b></h3>
     *
     * @param categoryRequestDto {@link CategoryRequestDto CategoryRequestDto.class} dto для создания/замены сущности
     * @return {@link CategoryResponseDto CategoryResponseDto.class} dto возвращаемой сущности
     * @throws UniqueViolationException если параметр name сущности уже существует
     * @throws ConflictException        если параметр name сущности {@link Category} связан с {@link Event}
     * @throws NotFoundException        если параметр id сущности {@link Category} не найден
     */
    @Override
    public CategoryResponseDto update(final Long id, final CategoryRequestDto categoryRequestDto) {
        var oldCategory = repository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        if (oldCategory.getName().equals(categoryRequestDto.getName())) {
            log.trace("Категория не изменяется. Фейковый запрос");
            return mapper.toCategoryResponseDto(oldCategory);
        }
        try {
            if (eventRepository.existsByCategoryId(id)) {
                log.info("Проверка существования события с категорией {}", oldCategory);
                throw new ConflictException("Category linked with event");
            }
            oldCategory.setName(categoryRequestDto.getName());
            log.info("Сохранение в бд категории {}", oldCategory);
            return mapper.toCategoryResponseDto(repository.save(oldCategory));
        } catch (Exception e) {
            throw new UniqueViolationException("name of category already exists");
        }
    }

    /**
     * Обращается к БД и возвращает список dto {@link Category Category.class} если они существуют
     * <p>{@link PublicCategoryController PublicCategoryController.class} - <b>основной контроллер</b></b></p>
     * <h3>GET /categories <b> - эндпоинт </b></h3>
     *
     * @param from параметр постраничного вывода
     * @param size параметр постраничного вывода
     * @return List {@link CategoryResponseDto CategoryResponseDto.class}
     */
    @Override
    public List<CategoryResponseDto> geAll(final Integer from, final Integer size) {
        Pageable page = PageBuilder.create(from, size);
        List<Category> categories = repository.findAll(page).getContent();
        if (categories.isEmpty()) {
            log.trace("Категории не найдены. Возвращен пустой список");
            return List.of();
        }
        log.info("Категории найдены. Происходит маппинг в dto");
        return categories.stream()
                .map(mapper::toCategoryResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Обращается к БД по id и возвращает dto {@link Category Category.class} если она существует
     * <p>{@link PublicCategoryController PublicCategoryController.class} - <b>основной контроллер</b></b></p>
     * <h3>GET /categories/{categoryId} <b> - эндпоинт </b></h3>
     *
     * @param id уникальный идентификатор(id) категории
     * @return {@link CategoryResponseDto CategoryResponseDto.class}
     * @throws NotFoundException если категория не найдена
     */
    @Override
    public CategoryResponseDto getById(final Long id) {
        return mapper.toCategoryResponseDto(repository.findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Category not found");
                }));
    }
}
