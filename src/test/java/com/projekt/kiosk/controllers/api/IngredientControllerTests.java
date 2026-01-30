package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.services.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class IngredientControllerTests {

    private static final String BASE_URL = "/api/v1/ingredients";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private IngredientService ingredientService;

    @Autowired
    public IngredientControllerTests(MockMvc mockMvc, IngredientService ingredientService) {
        this.mockMvc = mockMvc;
        this.ingredientService = ingredientService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateIngredientCorrectCode() throws Exception {
        IngredientEntity ingredient = TestDataUtil.createTestIngredientA();
        ingredient.setId(null);
        String json = objectMapper.writeValueAsString(ingredient);
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void testCreateIngredientCorrectReturnValue() throws Exception {
        IngredientEntity ingredient = TestDataUtil.createTestIngredientA();
        ingredient.setId(null);
        String json = objectMapper.writeValueAsString(ingredient);
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ingredient.getName()));

    }

    @Test
    public void testReadIngredientsCorrectCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testReadIngredientsCorrectReturnValue() throws Exception {
        IngredientEntity ingredient = TestDataUtil.createTestIngredientA();
        this.ingredientService.save(ingredient);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(ingredient.getName()));

    }

    @Test
    public void testReadOneIngredientCorrectReturnValue() throws Exception {
        IngredientEntity ingredient = TestDataUtil.createTestIngredientA();
        this.ingredientService.save(ingredient);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ingredient.getName()));

    }

    @Test
    public void testReadIngredientsCorrectCodeNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testReadOneIngredientCorrectCodeOk() throws Exception {
        IngredientEntity ingredient = TestDataUtil.createTestIngredientA();
        this.ingredientService.save(ingredient);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testUpdateIngredientCorrectCodeOk() throws Exception {
        IngredientEntity ingredientA = TestDataUtil.createTestIngredientA();
        this.ingredientService.save(ingredientA);
        IngredientEntity ingredientB = TestDataUtil.createTestIngredientB();
        String json = objectMapper.writeValueAsString(ingredientB);
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateIngredientCorrectCodeNotFound() throws Exception {
        IngredientEntity ingredientB = TestDataUtil.createTestIngredientB();
        String json = objectMapper.writeValueAsString(ingredientB);
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testUpdateIngredientCorrectValue() throws Exception {
        IngredientEntity ingredientA = TestDataUtil.createTestIngredientA();
        this.ingredientService.save(ingredientA);
        IngredientEntity ingredientB = TestDataUtil.createTestIngredientB();
        String json = objectMapper.writeValueAsString(ingredientB);
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ingredientB.getName()));

    }

    @Test
    public void testPatchIngredientCorrectCodeOk() throws Exception {
        IngredientEntity ingredientA = TestDataUtil.createTestIngredientA();
        this.ingredientService.save(ingredientA);
        IngredientEntity ingredientB = TestDataUtil.createTestIngredientB();

        String json = objectMapper.writeValueAsString(ingredientB);

        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/1")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPatchIngredientCorrectCodeNotFound() throws Exception {
        IngredientEntity ingredientB = TestDataUtil.createTestIngredientB();
        String json = objectMapper.writeValueAsString(ingredientB);
        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/1")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testPatchIngredientCorrectValue() throws Exception {
        IngredientEntity ingredientA = TestDataUtil.createTestIngredientA();
        this.ingredientService.save(ingredientA);
        IngredientEntity ingredientB = TestDataUtil.createTestIngredientB();
        ingredientB.setId(null);
        String json = objectMapper.writeValueAsString(ingredientB);
        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/" + ingredientA.getId())
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.jsonPath("$.id").value(ingredientA.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(ingredientB.getName()));

    }

    @Test
    public void testDeleteExistingIngredientCorrectCodeNoContent() throws Exception {
        IngredientEntity ingredientA = TestDataUtil.createTestIngredientA();
        this.ingredientService.save(ingredientA);
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + ingredientA.getId())
                .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteNotExistingIngredientCorrectCodeNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1123")
                .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
