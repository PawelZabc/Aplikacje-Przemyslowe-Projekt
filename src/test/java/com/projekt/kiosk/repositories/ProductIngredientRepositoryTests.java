package com.projekt.kiosk.repositories;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.entities.ProductIngredientEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductIngredientRepositoryTests {

    private final ProductIngredientRepository repository;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public ProductIngredientRepositoryTests(
            ProductIngredientRepository repository,
            ProductRepository productRepository,
            IngredientRepository ingredientRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Test
    public void testSaveProductIngredient() {
        ProductEntity product = productRepository.save(TestDataUtil.createTestProductA());
        IngredientEntity ingredient = ingredientRepository.save(TestDataUtil.createTestIngredientA());

        ProductIngredientEntity pi = ProductIngredientEntity.builder()
                .product(product)
                .ingredient(ingredient)
                .build();

        repository.save(pi);

        List<ProductIngredientEntity> result = repository.findByProductId(product.getId());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIngredient().getId())
                .isEqualTo(ingredient.getId());
    }
}
