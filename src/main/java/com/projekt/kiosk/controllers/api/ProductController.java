package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.dto.ProductDetailsDto;
import com.projekt.kiosk.dto.ProductDto;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.ProductService;
import com.projekt.kiosk.services.impl.ProductDetailsServiceImpl;
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
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductDetailsServiceImpl productDetailsService;
    private final Mapper<ProductEntity, ProductDto> productMapper;

    public ProductController(ProductService productService,
                             Mapper<ProductEntity, ProductDto> productMapper,
                             ProductDetailsServiceImpl productDetailsService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.productDetailsService = productDetailsService;
    }


    @Operation(summary = "Odczytaj wszystkie produkty",
            description = "Odczytuje listę wszystkich produktów")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista produktów zwrócona poprawnie")
    })
    @GetMapping
    public List<ProductEntity> getProducts() {
        List<ProductEntity> products = productService.readAll();
        log.info("GET /api/v1/products - sukces, liczba produktów: {}", products.size());
        return products;
    }


    @Operation(summary = "Utwórz nowy produkt",
            description = "Tworzy nowy produkt na podstawie przekazanego DTO")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produkt utworzony"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe")
    })
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {

        if (productDto.getName() == null || productDto.getName().isBlank()) {
            log.warn("POST /api/v1/products - niepoprawne dane: brak nazwy");
            throw new IllegalArgumentException("Name cannot be null or blank");
        }

        ProductEntity product = productMapper.mapFrom(productDto);
        ProductEntity saved = productService.save(product);

        log.info("POST /api/v1/products - sukces, utworzono produkt id={}", saved.getId());

        return new ResponseEntity<>(productMapper.mapTo(saved), HttpStatus.CREATED);
    }


    @Operation(summary = "Pobierz produkt po ID",
            description = "Zwraca pojedynczy produkt na podstawie ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt znaleziony"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Integer id) {

        if (id <= 0) {
            log.warn("GET /api/v1/products/{} - niepoprawne ID", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        ProductEntity product = productService.readOne(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product id=" + id + " not found"));

        log.info("GET /api/v1/products/{} - sukces", id);

        return ResponseEntity.ok(productMapper.mapTo(product));
    }


    @Operation(summary = "Aktualizuj produkt",
            description = "Aktualizuje cały produkt (PUT)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductDto productDto) {

        if (id <= 0) {
            log.warn("PUT /api/v1/products/{} - niepoprawne ID", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        if (!productService.exists(id)) {
            throw new ResourceNotFoundException("Product id=" + id + " not found");
        }

        if (productDto.getName() == null || productDto.getName().isBlank()) {
            log.warn("PUT /api/v1/products/{} - brak nazwy", id);
            throw new IllegalArgumentException("Name cannot be null or blank");
        }

        productDto.setId(id);
        ProductEntity updated = productService.save(productMapper.mapFrom(productDto));

        log.info("PUT /api/v1/products/{} - sukces", id);

        return ResponseEntity.ok(productMapper.mapTo(updated));
    }


    @Operation(summary = "Częściowa aktualizacja produktu",
            description = "Aktualizuje wybrane pola produktu (PATCH)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> patchProduct(
            @PathVariable Integer id,
            @RequestBody ProductDto productDto) {

        if (id <= 0) {
            log.warn("PATCH /api/v1/products/{} - niepoprawne ID", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        if (!productService.exists(id)) {
            throw new ResourceNotFoundException("Product id=" + id + " not found");
        }

        ProductEntity patched = productService.partialUpdate(
                id, productMapper.mapFrom(productDto));

        log.info("PATCH /api/v1/products/{} - sukces", id);

        return ResponseEntity.ok(productMapper.mapTo(patched));
    }


    @Operation(summary = "Usuń produkt",
            description = "Usuwa produkt na podstawie ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produkt usunięty"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {

        if (id <= 0) {
            log.warn("DELETE /api/v1/products/{} - niepoprawne ID", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        if (!productService.exists(id)) {
            throw new ResourceNotFoundException("Product id=" + id + " not found");
        }

        productService.delete(id);
        log.info("DELETE /api/v1/products/{} - sukces", id);

        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Pobierz szczegóły produktu",
            description = "Zwraca szczegóły produktu wraz ze składnikami i dodatkami")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Szczegóły produktu zwrócone"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne ID")
    })
    @GetMapping("/details/{id}")
    public ResponseEntity<ProductDetailsDto> getProductDetails(@PathVariable Integer id) {

        if (id <= 0) {
            log.warn("GET /api/v1/products/details/{} - niepoprawne ID", id);
            throw new IllegalArgumentException("ID must be positive");
        }

        log.info("GET /api/v1/products/details/{} - pobieranie szczegółów", id);

        return ResponseEntity.ok(productDetailsService.getProductDetails(id));
    }
}
