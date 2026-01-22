package com.projekt.kiosk.repositories;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.domain.Ingredient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
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
        Ingredient ingredient = TestDataUtil.createTestIngredientA();
        ingredientRepository.save(ingredient);
        Optional<Ingredient> result= ingredientRepository.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(ingredient);


    }

    @Test
    public void testFindAll(){
        Ingredient ingredientA = TestDataUtil.createTestIngredientA();
        ingredientRepository.save(ingredientA);
        Ingredient ingredientB = TestDataUtil.createTestIngredientB();
        ingredientRepository.save(ingredientB);
        Ingredient ingredientC = TestDataUtil.createTestIngredientC();
        ingredientRepository.save(ingredientC);

        Iterable<Ingredient> result = ingredientRepository.findAll();
        assertThat(result)
                .hasSize(3).containsExactly(ingredientA,ingredientB,ingredientC);
    }

    @Test
    public void testUpdate(){
        Ingredient ingredientA = TestDataUtil.createTestIngredientA();
        ingredientRepository.save(ingredientA);
        ingredientA.setName("updated name");
        ingredientRepository.save(ingredientA);
        Optional<Ingredient> result= ingredientRepository.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(ingredientA);
    }

    @Test
    public void testDeleteIngredient() {
        Ingredient ingredient = TestDataUtil.createTestIngredientA();
        ingredientRepository.save(ingredient);
        ingredientRepository.deleteById(ingredient.getId());
        Optional<Ingredient> result= ingredientRepository.findById(ingredient.getId());
        assertThat(result).isNotPresent();


    }

}
