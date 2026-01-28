package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.domain.ProductIngredientEntity;
import com.projekt.kiosk.dtos.ProductIngredientDto;
import com.projekt.kiosk.services.inpl.ProductIngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/{productId}/ingredients")
public class ProductIngredientController {

    private final ProductIngredientService service;

    public ProductIngredientController(ProductIngredientService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductIngredientEntity> getIngredients(
            @PathVariable Integer productId) {
        return service.readByProduct(productId);
    }

    @PostMapping
    public ResponseEntity<Void> addIngredient(
            @PathVariable Integer productId,
            @RequestBody ProductIngredientDto dto) {

        service.create(productId, dto.getIngredientId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(
            @PathVariable Integer productId,
            @PathVariable Integer ingredientId) {

        service.delete(productId, ingredientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

