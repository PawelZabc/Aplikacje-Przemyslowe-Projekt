package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.dto.CategoryDto;
import com.projekt.kiosk.entities.CategoryEntity;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.impl.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryServiceImpl categoryService;
    private final Mapper<CategoryEntity, CategoryDto> categoryMapper;

    public CategoryController(CategoryServiceImpl categoryService,
                              Mapper<CategoryEntity, CategoryDto> categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @Operation(summary = "Odczytaj wszystkie kategorie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista kategorii zwrócona poprawnie")
    })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> categories = categoryService.readAll().stream()
                .map(categoryMapper::mapTo)
                .toList();

        log.info("GET /api/v1/categories - sukces, liczba kategorii: {}", categories.size());
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Utwórz nową kategorię")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Kategoria utworzona"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryEntity created = categoryService.save(categoryMapper.mapFrom(categoryDto));

        log.info("POST /api/v1/categories - utworzono kategorię id={}", created.getId());
        return new ResponseEntity<>(categoryMapper.mapTo(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Pobierz kategorię po ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kategoria znaleziona"),
            @ApiResponse(responseCode = "404", description = "Kategoria nie istnieje")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer id) {

        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }

        CategoryEntity category = categoryService.readOne(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category id=" + id + " not found"));

        log.info("GET /api/v1/categories/{} - sukces", id);
        return ResponseEntity.ok(categoryMapper.mapTo(category));
    }

    @Operation(summary = "Aktualizuj kategorię")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryDto categoryDto) {

        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }

        if (!categoryService.exists(id)) {
            throw new ResourceNotFoundException("Category id=" + id + " not found");
        }

        categoryDto.setId(id);
        CategoryEntity saved = categoryService.save(categoryMapper.mapFrom(categoryDto));

        log.info("PUT /api/v1/categories/{} - sukces", id);
        return ResponseEntity.ok(categoryMapper.mapTo(saved));
    }

    @Operation(summary = "Częściowa aktualizacja kategorii")
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> patchCategory(
            @PathVariable Integer id,
            @RequestBody CategoryDto categoryDto) {

        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }

        CategoryEntity updated = categoryService.partialUpdate(
                id,
                categoryMapper.mapFrom(categoryDto)
        );

        log.info("PATCH /api/v1/categories/{} - sukces", id);
        return ResponseEntity.ok(categoryMapper.mapTo(updated));
    }

    @Operation(summary = "Usuń kategorię")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {

        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }

        if (!categoryService.exists(id)) {
            throw new ResourceNotFoundException("Category id=" + id + " not found");
        }

        categoryService.delete(id);
        log.info("DELETE /api/v1/categories/{} - sukces", id);

        return ResponseEntity.noContent().build();
    }
}
