package com.projekt.kiosk.dao.impl;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.domain.Ingredient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IngredientDaoIntegrationTests {

    private IngredientDaoImpl ingredientDaoMock;
    @Autowired
    public IngredientDaoIntegrationTests(IngredientDaoImpl ingredientDao){
        this.ingredientDaoMock = ingredientDao;
    }

    @Test
    public void testCreateIngredient() {
        Ingredient ingredient = TestDataUtil.createTestIngredientA();
        ingredientDaoMock.create(ingredient);
        Optional<Ingredient> result= ingredientDaoMock.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(ingredient);


    }

    @Test
    public void testFindAll(){
        Ingredient ingredientA = TestDataUtil.createTestIngredientA();
        ingredientDaoMock.create(ingredientA);
        Ingredient ingredientB = TestDataUtil.createTestIngredientB();
        ingredientDaoMock.create(ingredientB);
        Ingredient ingredientC = TestDataUtil.createTestIngredientC();
        ingredientDaoMock.create(ingredientC);

        List<Ingredient> result = ingredientDaoMock.find();
        assertThat(result)
                .hasSize(3).containsExactly(ingredientA,ingredientB,ingredientC);
    }

    @Test
    public void testUpdate(){
        Ingredient ingredientA = TestDataUtil.createTestIngredientA();
        ingredientDaoMock.create(ingredientA);
        ingredientA.setName("updated name");
        ingredientDaoMock.update(ingredientA);
        Optional<Ingredient> result= ingredientDaoMock.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(ingredientA);
    }

    @Test
    public void testDeleteIngredient() {
        Ingredient ingredient = TestDataUtil.createTestIngredientA();
        ingredientDaoMock.create(ingredient);
        ingredientDaoMock.delete(1);
        Optional<Ingredient> result= ingredientDaoMock.findById(1);
        assertThat(result).isNotPresent();


    }

}
