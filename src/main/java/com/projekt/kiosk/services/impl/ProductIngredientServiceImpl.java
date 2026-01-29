package com.projekt.kiosk.services.impl;

import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.entities.ProductIngredientEntity;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.repositories.IngredientRepository;
import com.projekt.kiosk.repositories.ProductIngredientRepository;
import com.projekt.kiosk.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductIngredientServiceImpl {

    private final ProductIngredientRepository repository;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    public ProductIngredientServiceImpl(
            ProductIngredientRepository repository,
            ProductRepository productRepository,
            IngredientRepository ingredientRepository
    ) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<ProductIngredientEntity> readByProduct(Integer productId) {
        return repository.findByProductId(productId);
    }

    public ProductIngredientEntity create(Integer productId, Integer ingredientId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        IngredientEntity ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient not found"));

        if (repository.existsByProductIdAndIngredientId(productId, ingredientId)) {
            throw new IllegalArgumentException("Ingredient already assigned to product");
        }

        ProductIngredientEntity pi = ProductIngredientEntity.builder()
                .product(product)
                .ingredient(ingredient)
                .build();

        return repository.save(pi);
    }

    public void delete(Integer productId, Integer ingredientId) {
        repository.deleteByProductIdAndIngredientId(productId, ingredientId);
    }
}

