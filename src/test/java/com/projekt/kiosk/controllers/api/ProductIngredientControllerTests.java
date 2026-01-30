package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.dto.ProductIngredientDto;
import com.projekt.kiosk.repositories.IngredientRepository;
import com.projekt.kiosk.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductIngredientControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public ProductIngredientControllerTests(
            MockMvc mockMvc,
            ProductRepository productRepository,
            IngredientRepository ingredientRepository) {
        this.mockMvc = mockMvc;
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddProductIngredient() throws Exception {
        ProductEntity product = productRepository.save(TestDataUtil.createTestProductA());
        IngredientEntity ingredient = ingredientRepository.save(TestDataUtil.createTestIngredientA());

        ProductIngredientDto dto = ProductIngredientDto.builder()
                .ingredientId(ingredient.getId())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/products/" + product.getId() + "/ingredients")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testGetProductIngredients() throws Exception {
        ProductEntity product = productRepository.save(TestDataUtil.createTestProductA());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/products/" + product.getId() + "/ingredients"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
