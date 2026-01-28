package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.domain.ProductEntity;
import com.projekt.kiosk.dtos.ProductDetailsDto;
import com.projekt.kiosk.dtos.ProductDto;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.ProductService;
import com.projekt.kiosk.services.inpl.ProductDetailsService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log
public class ProductController {

    private final ProductService productService;
    private final ProductDetailsService productDetailsService;
    private final Mapper<ProductEntity, ProductDto> productMapper;

    public ProductController(ProductService productService,
                             Mapper<ProductEntity, ProductDto> productMapper,
                             ProductDetailsService productDetailsService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.productDetailsService = productDetailsService;
    }

    @GetMapping("/products")
    public List<ProductEntity> getProducts() {
        return productService.readAll();
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductEntity product = productMapper.mapFrom(productDto);
        ProductEntity saved = productService.save(product);

        return new ResponseEntity<>(productMapper.mapTo(saved), HttpStatus.CREATED);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Integer id) {
        return productService.readOne(id)
                .map(p -> new ResponseEntity<>(productMapper.mapTo(p), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductDto productDto) {

        if (!productService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productDto.setId(id);
        ProductEntity updated = productService.save(productMapper.mapFrom(productDto));

        return new ResponseEntity<>(productMapper.mapTo(updated), HttpStatus.OK);
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<ProductDto> patchProduct(
            @PathVariable Integer id,
            @RequestBody ProductDto productDto) {

        if (!productService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductEntity patched = productService.partialUpdate(
                id, productMapper.mapFrom(productDto));

        return new ResponseEntity<>(productMapper.mapTo(patched), HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("products/details/{id}")
    public ResponseEntity<ProductDetailsDto> getProductDetails(@PathVariable Integer id) {
        return ResponseEntity.ok(productDetailsService.getProductDetails(id));
    }
}

