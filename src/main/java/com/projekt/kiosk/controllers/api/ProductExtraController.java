package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.domain.ProductExtraEntity;
import com.projekt.kiosk.dtos.ProductExtraDto;
import com.projekt.kiosk.services.inpl.ProductExtraService;
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
@RequestMapping("/api/v1/products/{productId}/extras")
public class ProductExtraController {

    private final ProductExtraService service;

    public ProductExtraController(ProductExtraService service) {
        this.service = service;
    }


    @Operation(summary = "Pobierz dodatki produktu",
            description = "Zwraca listę dodatków przypisanych do produktu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista dodatków zwrócona poprawnie"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne ID produktu"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje")
    })
    @GetMapping
    public List<ProductExtraEntity> getExtras(@PathVariable Integer productId) {

        if (productId <= 0) {
            log.warn("GET /api/v1/products/{}/extras - niepoprawne ID produktu", productId);
            throw new IllegalArgumentException("Product ID must be positive");
        }

        List<ProductExtraEntity> extras = service.readByProduct(productId);

        if (extras.isEmpty()) {
            log.warn("GET /api/v1/products/{}/extras - brak dodatków lub produkt nie istnieje", productId);
        } else {
            log.info("GET /api/v1/products/{}/extras - sukces, liczba dodatków: {}",
                    productId, extras.size());
        }

        return extras;
    }


    @Operation(summary = "Dodaj dodatek do produktu",
            description = "Przypisuje dodatek do produktu")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dodatek przypisany do produktu"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
            @ApiResponse(responseCode = "404", description = "Produkt lub dodatek nie istnieje")
    })
    @PostMapping
    public ResponseEntity<Void> addExtra(
            @PathVariable Integer productId,
            @RequestBody ProductExtraDto dto) {

        if (productId <= 0) {
            log.warn("POST /api/v1/products/{}/extras - niepoprawne ID produktu", productId);
            throw new IllegalArgumentException("Product ID must be positive");
        }

        if (dto.getExtraId() == null || dto.getExtraId() <= 0) {
            log.warn("POST /api/v1/products/{}/extras - niepoprawne ID dodatku", productId);
            throw new IllegalArgumentException("Extra ID must be positive");
        }

        service.create(productId, dto.getExtraId());

        log.info("POST /api/v1/products/{}/extras - sukces, dodano extraId={}",
                productId, dto.getExtraId());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @Operation(summary = "Usuń dodatek z produktu",
            description = "Usuwa przypisanie dodatku do produktu")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dodatek usunięty z produktu"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne ID"),
            @ApiResponse(responseCode = "404", description = "Produkt lub dodatek nie istnieje")
    })
    @DeleteMapping("/{extraId}")
    public ResponseEntity<Void> deleteExtra(
            @PathVariable Integer productId,
            @PathVariable Integer extraId) {

        if (productId <= 0) {
            log.warn("DELETE /api/v1/products/{}/extras/{} - niepoprawne ID produktu",
                    productId, extraId);
            throw new IllegalArgumentException("Product ID must be positive");
        }

        if (extraId <= 0) {
            log.warn("DELETE /api/v1/products/{}/extras/{} - niepoprawne ID dodatku",
                    productId, extraId);
            throw new IllegalArgumentException("Extra ID must be positive");
        }

        service.delete(productId, extraId);

        log.info("DELETE /api/v1/products/{}/extras/{} - sukces",
                productId, extraId);

        return ResponseEntity.noContent().build();
    }
}
