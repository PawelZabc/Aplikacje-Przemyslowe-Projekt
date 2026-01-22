//package com.projekt.kiosk.dao;
//
//import com.projekt.kiosk.TestDataUtil;
////import com.projekt.kiosk.dao.impl.IngredientDaoImpl;
//import com.projekt.kiosk.domain.Ingredient;
//import com.projekt.kiosk.repositories.IngredientRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//public class IngredientDaoTests {
//    private IngredientRepository ingredientRepository;
//    public IngredientDaoIntegrationTests(IngredientRepository ingredientRepository) {
//        this.ingredientRepository = ingredientRepository;
//    }
//
//    @Mock
//    private JdbcTemplate jdbcTemplate;
//
//    @InjectMocks
//    private IngredientDaoImpl ingredientDaoMock;
//    @Test
//    public void testAddIngredient() {
//        Ingredient ingredient = TestDataUtil.createTestIngredientA();
//
//        ingredientDaoMock.create(ingredient);
//        verify(jdbcTemplate).update(
//                eq("insert into ingredients (name) values (?)"),
//                eq(ingredient.getName()));
//
//
//    }
//
//    @Test
//    public void testFindIngredientById() {
//        Ingredient ingredient = TestDataUtil.createTestIngredientA();
//        ingredientDaoMock.findById(1);
//        verify(jdbcTemplate).query(
//                eq("select id, name from ingredients where id = ? limit 1"),
//                ArgumentMatchers.<IngredientDaoImpl.IngredientMapper>any(),
//                eq(1)
//        );
//    }
//    @Test
//    public void testFindIngredients(){
//        ingredientDaoMock.find();
//        verify(jdbcTemplate).query(eq("select id, name from ingredients"),
//                ArgumentMatchers.<IngredientDaoImpl.IngredientMapper>any());
//    }
//
//    @Test
//    public void testUpdateIngredient() {
//        Ingredient ingredientA = TestDataUtil.createTestIngredientA();
//        ingredientDaoMock.create(ingredientA);
//        ingredientA.setName("updated name");
//        ingredientDaoMock.update(ingredientA);
//        verify(jdbcTemplate).update(
//                eq("update ingredients set name = ? where id = ?"),
//                eq("updated name"),
//                eq(ingredientA.getId()));
//    }
//
//    @Test
//    public void testDeleteIngredient() {
//        Ingredient ingredient = TestDataUtil.createTestIngredientA();
//        ingredientDaoMock.create(ingredient);
//        ingredientDaoMock.delete(1);
//        verify(jdbcTemplate).update("delete from ingredients where id = ?", ingredient.getId());
//    }
//}
