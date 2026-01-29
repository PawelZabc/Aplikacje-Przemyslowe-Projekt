package com.projekt.kiosk.services.impl;

import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.entities.ProductExtraEntity;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.repositories.ExtraRepository;
import com.projekt.kiosk.repositories.ProductExtraRepository;
import com.projekt.kiosk.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductExtraServiceImpl {

    private final ProductExtraRepository repository;
    private final ProductRepository productRepository;
    private final ExtraRepository extraRepository;

    public ProductExtraServiceImpl(
            ProductExtraRepository repository,
            ProductRepository productRepository,
            ExtraRepository extraRepository
    ) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.extraRepository = extraRepository;
    }

    public List<ProductExtraEntity> readByProduct(Integer productId) {
        return repository.findByProductId(productId);
    }

    public ProductExtraEntity create(Integer productId, Integer extraId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ExtraEntity extra = extraRepository.findById(extraId)
                .orElseThrow(() -> new ResourceNotFoundException("Extra not found"));

        if (repository.existsByProductIdAndExtraId(productId, extraId)) {
            throw new IllegalArgumentException("Extra already assigned to product");
        }

        ProductExtraEntity pe = ProductExtraEntity.builder()
                .product(product)
                .extra(extra)
                .build();

        return repository.save(pe);
    }

    public void delete(Integer productId, Integer extraId) {
        repository.deleteByProductIdAndExtraId(productId, extraId);
    }
}

