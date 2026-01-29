package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.entities.ProductIngredientEntity;
import com.projekt.kiosk.dto.ProductIngredientDto;
import com.projekt.kiosk.services.impl.ProductIngredientServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/products/{productId}/ingredients")
public class ProductIngredientController {

    private final ProductIngredientServiceImpl service;

    public ProductIngredientController(ProductIngredientServiceImpl service) {
        this.service = service;
    }


    @Operation(
            summary = "Pobierz składniki produktu",
            description = "Zwraca listę składników przypisanych do produktu"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista składników zwrócona poprawnie"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne ID produktu"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje")
    })
    @GetMapping
    public List<ProductIngredientEntity> getIngredients(
            @PathVariable Integer productId) {

        if (productId <= 0) {
            log.warn("GET /api/v1/products/{}/ingredients - niepoprawne ID produktu", productId);
            throw new IllegalArgumentException("Product ID must be positive");
        }

        List<ProductIngredientEntity> ingredients = service.readByProduct(productId);

        if (ingredients.isEmpty()) {
            log.warn("GET /api/v1/products/{}/ingredients - brak składników lub produkt nie istnieje", productId);
        } else {
            log.info("GET /api/v1/products/{}/ingredients - sukces, liczba składników: {}",
                    productId, ingredients.size());
        }

        return ingredients;
    }


    @Operation(
            summary = "Dodaj składnik do produktu",
            description = "Przypisuje składnik do produktu"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Składnik dodany do produktu"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
            @ApiResponse(responseCode = "404", description = "Produkt lub składnik nie istnieje")
    })
    @PostMapping
    public ResponseEntity<Void> addIngredient(
            @PathVariable Integer productId,
            @RequestBody ProductIngredientDto dto) {

        if (productId <= 0) {
            log.warn("POST /api/v1/products/{}/ingredients - niepoprawne ID produktu", productId);
            throw new IllegalArgumentException("Product ID must be positive");
        }

        if (dto.getIngredientId() == null || dto.getIngredientId() <= 0) {
            log.warn("POST /api/v1/products/{}/ingredients - niepoprawne ID składnika", productId);
            throw new IllegalArgumentException("Ingredient ID must be positive");
        }

        service.create(productId, dto.getIngredientId());

        log.info("POST /api/v1/products/{}/ingredients - sukces, dodano ingredientId={}",
                productId, dto.getIngredientId());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(
            summary = "Usuń składnik z produktu",
            description = "Usuwa przypisanie składnika do produktu"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Składnik usunięty z produktu"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne ID"),
            @ApiResponse(responseCode = "404", description = "Produkt lub składnik nie istnieje")
    })
    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(
            @PathVariable Integer productId,
            @PathVariable Integer ingredientId) {

        if (productId <= 0) {
            log.warn("DELETE /api/v1/products/{}/ingredients/{} - niepoprawne ID produktu",
                    productId, ingredientId);
            throw new IllegalArgumentException("Product ID must be positive");
        }

        if (ingredientId <= 0) {
            log.warn("DELETE /api/v1/products/{}/ingredients/{} - niepoprawne ID składnika",
                    productId, ingredientId);
            throw new IllegalArgumentException("Ingredient ID must be positive");
        }

        service.delete(productId, ingredientId);

        log.info("DELETE /api/v1/products/{}/ingredients/{} - sukces",
                productId, ingredientId);

        return ResponseEntity.noContent().build();
    }
}
