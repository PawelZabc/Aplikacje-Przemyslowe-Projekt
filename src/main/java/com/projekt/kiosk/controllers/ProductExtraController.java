package com.projekt.kiosk.controllers;

import com.projekt.kiosk.domain.ProductExtraEntity;
import com.projekt.kiosk.dtos.ProductExtraDto;
import com.projekt.kiosk.services.inpl.ProductExtraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/{productId}/extras")
public class ProductExtraController {

    private final ProductExtraService service;

    public ProductExtraController(ProductExtraService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductExtraEntity> getExtras(
            @PathVariable Integer productId) {
        return service.readByProduct(productId);
    }

    @PostMapping
    public ResponseEntity<Void> addExtra(
            @PathVariable Integer productId,
            @RequestBody ProductExtraDto dto) {

        service.create(productId, dto.getExtraId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{extraId}")
    public ResponseEntity<Void> deleteExtra(
            @PathVariable Integer productId,
            @PathVariable Integer extraId) {

        service.delete(productId, extraId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

