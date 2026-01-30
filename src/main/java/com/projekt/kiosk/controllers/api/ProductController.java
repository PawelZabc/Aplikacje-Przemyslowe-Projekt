package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.dto.ProductDto;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final Mapper<ProductEntity, ProductDto> productMapper;

    public ProductController(ProductService productService,
                             Mapper<ProductEntity, ProductDto> productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Operation(summary = "Odczytaj wszystkie produkty", description = "Zwraca listę wszystkich produktów z opcjonalną paginacją")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista produktów zwrócona poprawnie")
    })
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productsPage = productService.readAll(pageable);

        List<ProductDto> products = productsPage.getContent().stream()
                .map(productMapper::mapTo)
                .toList();

        log.info("GET /api/v1/products - page={}, size={}, returned={}",
                page, size, products.size());

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(productsPage.getTotalElements()))
                .body(products);
    }

    @Operation(summary = "Pobierz produkt po ID", description = "Zwraca pojedynczy produkt po jego ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt znaleziony"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        ProductEntity product = productService.readOne(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product id=" + id + " not found"));

        log.info("GET /api/v1/products/{} - sukces", id);
        return ResponseEntity.ok(productMapper.mapTo(product));
    }

    @Operation(summary = "Utwórz nowy produkt", description = "Tworzy nowy produkt na podstawie przekazanego DTO")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produkt utworzony"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe")
    })
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto dto) {
        ProductEntity product = productMapper.mapFrom(dto);
        ProductEntity saved = productService.save(product);

        log.info("POST /api/v1/products - sukces, utworzono produkt id={}", saved.getId());
        return new ResponseEntity<>(productMapper.mapTo(saved), HttpStatus.CREATED);
    }

    @Operation(summary = "Aktualizuj produkt", description = "Aktualizuje cały produkt (PUT)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductDto dto) {

        if (!productService.exists(id)) {
            throw new ResourceNotFoundException("Product id=" + id + " not found");
        }

        dto.setId(id);
        ProductEntity saved = productService.save(productMapper.mapFrom(dto));

        log.info("PUT /api/v1/products/{} - sukces", id);
        return ResponseEntity.ok(productMapper.mapTo(saved));
    }

    @Operation(summary = "Częściowa aktualizacja produktu", description = "Aktualizuje wybrane pola produktu (PATCH)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt zaktualizowany"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne dane")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDto> patchProduct(
            @PathVariable Integer id,
            @RequestBody ProductDto dto) {

        ProductEntity updated = productService.partialUpdate(id, productMapper.mapFrom(dto));

        log.info("PATCH /api/v1/products/{} - sukces", id);
        return ResponseEntity.ok(productMapper.mapTo(updated));
    }

    @Operation(summary = "Usuń produkt", description = "Usuwa produkt na podstawie ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produkt usunięty"),
            @ApiResponse(responseCode = "404", description = "Produkt nie istnieje")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        if (!productService.exists(id)) {
            throw new ResourceNotFoundException("Product id=" + id + " not found");
        }

        productService.delete(id);

        log.info("DELETE /api/v1/products/{} - sukces", id);
        return ResponseEntity.noContent().build();
    }
}
