package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.CategoryEntity;
import com.projekt.kiosk.services.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class CategoryControllerTests {

    private static final String BASE_URL = "/api/v1/categories";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final CategoryServiceImpl categoryService;

    @Autowired
    public CategoryControllerTests(MockMvc mockMvc, CategoryServiceImpl categoryService) {
        this.mockMvc = mockMvc;
        this.categoryService = categoryService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateCategoryCorrectCode() throws Exception {
        CategoryEntity category = TestDataUtil.createTestCategoryA();
        category.setId(null);
        String json = objectMapper.writeValueAsString(category);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testCreateCategoryCorrectReturnValue() throws Exception {
        CategoryEntity category = TestDataUtil.createTestCategoryA();
        category.setId(null);
        String json = objectMapper.writeValueAsString(category);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(category.getName()));
    }

    @Test
    public void testReadCategoriesCorrectCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testReadCategoriesCorrectReturnValue() throws Exception {
        CategoryEntity category = TestDataUtil.createTestCategoryA();
        this.categoryService.save(category);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(category.getName()));
    }

    @Test
    public void testReadOneCategoryCorrectReturnValue() throws Exception {
        CategoryEntity category = TestDataUtil.createTestCategoryA();
        this.categoryService.save(category);

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(category.getName()));
    }

    @Test
    public void testReadCategoryNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateCategoryCorrectCodeOk() throws Exception {
        CategoryEntity categoryA = TestDataUtil.createTestCategoryA();
        this.categoryService.save(categoryA);
        CategoryEntity categoryB = TestDataUtil.createTestCategoryB();
        String json = objectMapper.writeValueAsString(categoryB);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateCategoryNotFound() throws Exception {
        CategoryEntity categoryB = TestDataUtil.createTestCategoryB();
        String json = objectMapper.writeValueAsString(categoryB);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateCategoryCorrectValue() throws Exception {
        CategoryEntity categoryA = TestDataUtil.createTestCategoryA();
        this.categoryService.save(categoryA);
        CategoryEntity categoryB = TestDataUtil.createTestCategoryB();
        String json = objectMapper.writeValueAsString(categoryB);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(categoryB.getName()));
    }

    @Test
    public void testPatchCategoryCorrectCodeOk() throws Exception {
        CategoryEntity categoryA = TestDataUtil.createTestCategoryA();
        this.categoryService.save(categoryA);
        CategoryEntity categoryB = TestDataUtil.createTestCategoryB();
        String json = objectMapper.writeValueAsString(categoryB);

        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPatchCategoryNotFound() throws Exception {
        CategoryEntity categoryB = TestDataUtil.createTestCategoryB();
        String json = objectMapper.writeValueAsString(categoryB);

        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testPatchCategoryCorrectValue() throws Exception {
        CategoryEntity categoryA = TestDataUtil.createTestCategoryA();
        this.categoryService.save(categoryA);
        CategoryEntity categoryB = TestDataUtil.createTestCategoryB();
        categoryB.setId(null);
        String json = objectMapper.writeValueAsString(categoryB);

        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(categoryA.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(categoryB.getName()));
    }

    @Test
    public void testDeleteExistingCategoryCorrectCodeNoContent() throws Exception {
        CategoryEntity category = TestDataUtil.createTestCategoryA();
        this.categoryService.save(category);

        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteNotExistingCategoryCorrectCodeNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/123")
                .with(user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
