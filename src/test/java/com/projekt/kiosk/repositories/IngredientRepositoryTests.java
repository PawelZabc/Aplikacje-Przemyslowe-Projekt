package com.projekt.kiosk.repositories;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.IngredientEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IngredientRepositoryTests {

    @Mock
    private JdbcTemplate jdbcTemplate;
    private IngredientRepository ingredientRepository;

    @Autowired
    public IngredientRepositoryTests(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Test
    public void testSaveIngredient() {
        IngredientEntity ingredient = TestDataUtil.createTestIngredientA();
        ingredientRepository.save(ingredient);
        Optional<IngredientEntity> result = ingredientRepository.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(ingredient);

    }

    @Test
    public void testFindAll() {
        IngredientEntity ingredientA = TestDataUtil.createTestIngredientA();
        ingredientRepository.save(ingredientA);
        IngredientEntity ingredientB = TestDataUtil.createTestIngredientB();
        ingredientRepository.save(ingredientB);
        IngredientEntity ingredientC = TestDataUtil.createTestIngredientC();
        ingredientRepository.save(ingredientC);

        Iterable<IngredientEntity> result = ingredientRepository.findAll();
        assertThat(result)
                .hasSize(3).containsExactly(ingredientA, ingredientB, ingredientC);
    }

    @Test
    public void testUpdate() {
        IngredientEntity ingredientA = TestDataUtil.createTestIngredientA();
        ingredientRepository.save(ingredientA);
        ingredientA.setName("updated name");
        ingredientRepository.save(ingredientA);
        Optional<IngredientEntity> result = ingredientRepository.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(ingredientA);
    }

    @Test
    public void testDeleteIngredient() {
        IngredientEntity ingredient = TestDataUtil.createTestIngredientA();
        ingredientRepository.save(ingredient);
        ingredientRepository.deleteById(ingredient.getId());
        Optional<IngredientEntity> result = ingredientRepository.findById(ingredient.getId());
        assertThat(result).isNotPresent();
    }

}
