package com.projekt.kiosk.services.inpl;

import com.projekt.kiosk.domain.IngredientEntity;
import com.projekt.kiosk.domain.ProductEntity;
import com.projekt.kiosk.domain.ProductIngredientEntity;
import com.projekt.kiosk.repositories.IngredientRepository;
import com.projekt.kiosk.repositories.ProductIngredientRepository;
import com.projekt.kiosk.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductIngredientService {

    private final ProductIngredientRepository repository;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    public ProductIngredientService(
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
                .orElseThrow(() -> new RuntimeException("Product not found"));

        IngredientEntity ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        if (repository.existsByProductIdAndIngredientId(productId, ingredientId)) {
            throw new RuntimeException("Ingredient already assigned to product");
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

