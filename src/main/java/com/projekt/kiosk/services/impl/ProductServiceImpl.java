package com.projekt.kiosk.services.impl;

import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.repositories.ProductRepository;
import com.projekt.kiosk.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public ProductEntity save(ProductEntity product) {
        log.info("Saving product: {}", product.getName());
        ProductEntity saved = productRepository.save(product);
        log.debug("Product saved with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductEntity> readAll() {
        log.debug("Fetching all products");
        List<ProductEntity> products = StreamSupport.stream(
                productRepository.findAll().spliterator(), false).collect(Collectors.toList());
        log.info("Found {} products", products.size());
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductEntity> readAll(Pageable pageable) {
        log.debug("Fetching products with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductEntity> readOne(Integer id) {
        log.debug("Fetching product by id: {}", id);
        Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isEmpty()) {
            log.debug("Product not found: id={}", id);
        }
        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Integer id) {
        boolean exists = productRepository.existsById(id);
        log.debug("Product exists check: id={}, exists={}", id, exists);
        return exists;
    }

    @Override
    @Transactional
    public ProductEntity partialUpdate(Integer id, ProductEntity product) {
        log.info("Partial update for product id: {}", id);
        product.setId(id);

        return productRepository.findById(id).map(existing -> {
            Optional.ofNullable(product.getName()).ifPresent(name -> {
                log.debug("Updating product name: {} -> {}", existing.getName(), name);
                existing.setName(name);
            });
            Optional.ofNullable(product.getPriceCents()).ifPresent(price -> {
                log.debug("Updating product price: {} -> {}", existing.getPriceCents(), price);
                existing.setPriceCents(price);
            });
            return productRepository.save(existing);
        }).orElseThrow(() -> {
            log.error("Product not found for partial update: id={}", id);
            return new ResourceNotFoundException("Product id=" + id + " not found");
        });
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting product: id={}", id);
        productRepository.deleteById(id);
        log.debug("Product deleted: id={}", id);
    }
}
