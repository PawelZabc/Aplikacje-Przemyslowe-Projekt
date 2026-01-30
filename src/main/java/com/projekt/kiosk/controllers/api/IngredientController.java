package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.dto.IngredientDto;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/ingredients")
public class IngredientController {

        private final IngredientService ingredientService;
        private final Mapper<IngredientEntity, IngredientDto> ingredientMapper;

        public IngredientController(IngredientService ingredientService,
                        Mapper<IngredientEntity, IngredientDto> ingredientMapper) {
                this.ingredientService = ingredientService;
                this.ingredientMapper = ingredientMapper;
        }

        @Operation(summary = "Odczytaj wszystkie składniki", description = "Odczytuje listę wszystkich składników")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista składników zwrócona poprawnie")
        })
        @GetMapping
        public ResponseEntity<List<IngredientDto>> getIngredients() {
                List<IngredientDto> ingredients = ingredientService.readAll().stream()
                                .map(ingredientMapper::mapTo)
                                .toList();
                log.info("GET /api/v1/ingredients - sukces, liczba składników: {}", ingredients.size());
                return ResponseEntity.ok(ingredients);
        }

        @Operation(summary = "Utwórz nowy składnik", description = "Tworzy nowy składnik na podstawie przekazanego DTO")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Składnik utworzony"),
                        @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe")
        })
        @PostMapping
        public ResponseEntity<IngredientDto> createIngredient(@Valid @RequestBody IngredientDto ingredientDto) {

                IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredientDto);
                IngredientEntity createdIngredient = ingredientService.save(ingredientEntity);

                log.info("POST /api/v1/ingredients - sukces, utworzono składnik id={}",
                                createdIngredient.getId());

                return new ResponseEntity<>(
                                ingredientMapper.mapTo(createdIngredient),
                                HttpStatus.CREATED);
        }

        @Operation(summary = "Pobierz składnik po ID", description = "Zwraca pojedynczy składnik na podstawie ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Składnik znaleziony"),
                        @ApiResponse(responseCode = "404", description = "Składnik nie istnieje"),
                        @ApiResponse(responseCode = "400", description = "Niepoprawne ID")
        })
        @GetMapping("/{id}")
        public ResponseEntity<IngredientDto> getIngredientById(@PathVariable Integer id) {

                if (id <= 0) {
                        log.warn("GET /api/v1/ingredients/{} - niepoprawne ID", id);
                        throw new IllegalArgumentException("ID must be positive");
                }

                IngredientEntity ingredient = ingredientService.readOne(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Ingredient id=" + id + " not found"));

                log.info("GET /api/v1/ingredients/{} - sukces", id);

                return ResponseEntity.ok(ingredientMapper.mapTo(ingredient));
        }

        @Operation(summary = "Aktualizuj składnik", description = "Aktualizuje cały składnik (PUT)")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Składnik zaktualizowany"),
                        @ApiResponse(responseCode = "404", description = "Składnik nie istnieje"),
                        @ApiResponse(responseCode = "400", description = "Niepoprawne dane")
        })
        @PutMapping("/{id}")
        public ResponseEntity<IngredientDto> updateIngredient(
                        @PathVariable Integer id,
                        @Valid @RequestBody IngredientDto ingredientDto) {

                if (id <= 0) {
                        log.warn("PUT /api/v1/ingredients/{} - niepoprawne ID", id);
                        throw new IllegalArgumentException("ID must be positive");
                }

                if (!ingredientService.exists(id)) {
                        throw new ResourceNotFoundException("Ingredient id=" + id + " not found");
                }

                ingredientDto.setId(id);
                IngredientEntity savedIngredient = ingredientService.save(ingredientMapper.mapFrom(ingredientDto));

                log.info("PUT /api/v1/ingredients/{} - sukces", id);

                return ResponseEntity.ok(ingredientMapper.mapTo(savedIngredient));
        }

        @Operation(summary = "Częściowa aktualizacja składnika", description = "Aktualizuje wybrane pola składnika (PATCH)")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Składnik zaktualizowany"),
                        @ApiResponse(responseCode = "404", description = "Składnik nie istnieje"),
                        @ApiResponse(responseCode = "400", description = "Niepoprawne dane")
        })
        @PatchMapping("/{id}")
        public ResponseEntity<IngredientDto> patchIngredient(
                        @PathVariable Integer id,
                        @RequestBody IngredientDto ingredientDto) {

                if (id <= 0) {
                        log.warn("PATCH /api/v1/ingredients/{} - niepoprawne ID", id);
                        throw new IllegalArgumentException("ID must be positive");
                }

                if (!ingredientService.exists(id)) {
                        throw new ResourceNotFoundException("Ingredient id=" + id + " not found");
                }

                IngredientEntity updatedIngredient = ingredientService.partialUpdate(id,
                                ingredientMapper.mapFrom(ingredientDto));

                log.info("PATCH /api/v1/ingredients/{} - sukces", id);

                return ResponseEntity.ok(ingredientMapper.mapTo(updatedIngredient));
        }

        @Operation(summary = "Usuń składnik", description = "Usuwa składnik na podstawie ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Składnik usunięty"),
                        @ApiResponse(responseCode = "404", description = "Składnik nie istnieje"),
                        @ApiResponse(responseCode = "400", description = "Niepoprawne ID")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteIngredient(@PathVariable Integer id) {

                if (id <= 0) {
                        log.warn("DELETE /api/v1/ingredients/{} - niepoprawne ID", id);
                        throw new IllegalArgumentException("ID must be positive");
                }

                if (!ingredientService.exists(id)) {
                        throw new ResourceNotFoundException("Ingredient id=" + id + " not found");
                }

                ingredientService.delete(id);
                log.info("DELETE /api/v1/ingredients/{} - sukces", id);

                return ResponseEntity.noContent().build();
        }
}
