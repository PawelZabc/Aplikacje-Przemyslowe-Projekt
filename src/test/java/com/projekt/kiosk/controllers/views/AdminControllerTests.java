package com.projekt.kiosk.controllers.views;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.projekt.kiosk.services.ProductService;
import com.projekt.kiosk.services.IngredientService;
import com.projekt.kiosk.services.ExtraService;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.impl.ProductIngredientServiceImpl;
import com.projekt.kiosk.services.impl.ProductExtraServiceImpl;
import com.projekt.kiosk.mappers.ExtraMapper;
import com.projekt.kiosk.mappers.IngredientMapper;
import com.projekt.kiosk.services.impl.CategoryServiceImpl;
import com.projekt.kiosk.KioskApplication;

@SpringBootTest(classes = KioskApplication.class)
@AutoConfigureMockMvc
class AdminControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private IngredientService ingredientService;

    @MockitoBean
    private ExtraService extraService;

    @MockitoBean
    private Mapper productMapper;

    @MockitoBean
    private ProductIngredientServiceImpl productIngredientService;

    @MockitoBean
    private ProductExtraServiceImpl productExtraService;

    @MockitoBean
    private CategoryServiceImpl categoryService;

    @MockitoBean
    private Mapper categoryMapper;

    @Test
    @DisplayName("GET /admin should return admin view")
    void shouldReturnAdminView() throws Exception {
        when(productService.readAll()).thenReturn(List.of());
        when(extraService.readAll()).thenReturn(List.of());
        when(ingredientService.readAll()).thenReturn(List.of());
        when(categoryService.readAll()).thenReturn(List.of());

        mockMvc.perform(get("/admin")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/admin"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("products", "extras", "ingredients",
                        "categories"));
    }

    // --- Products ---

    @Test
    @DisplayName("GET /admin/products returns create product view")
    void createProductPage_returnsView() throws Exception {
        mockMvc.perform(get("/admin/products")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/create-product"));
    }

    @Test
    @DisplayName("POST /admin/products saves product and redirects")
    void createProduct_savesProductAndRedirects() throws Exception {
        when(categoryService.readOne(1))
                .thenReturn(java.util.Optional.of(new com.projekt.kiosk.entities.CategoryEntity()));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/products")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN"))
                .param("name", "New Burger")
                .param("priceCents", "1500")
                .param("categoryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        org.mockito.Mockito.verify(productService)
                .save(org.mockito.ArgumentMatchers.any(com.projekt.kiosk.entities.ProductEntity.class));
    }

    @Test
    @DisplayName("GET /admin/products/{id}/edit returns edit view")
    void editProductPage_returnsView_withModel() throws Exception {
        com.projekt.kiosk.entities.ProductEntity product = new com.projekt.kiosk.entities.ProductEntity();
        product.setId(1);
        when(productService.readOne(1)).thenReturn(java.util.Optional.of(product));
        when(categoryService.readAll()).thenReturn(List.of());
        when(productMapper.mapTo(product)).thenReturn(new com.projekt.kiosk.dto.ProductDto());

        mockMvc.perform(get("/admin/products/1/edit")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/edit-product"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("product", "categories"));
    }

    @Test
    @DisplayName("POST /admin/products/{id}/edit updates product and redirects")
    void updateProduct_updatesAndRedirects() throws Exception {
        com.projekt.kiosk.entities.ProductEntity product = new com.projekt.kiosk.entities.ProductEntity();
        product.setId(1);
        when(productService.readOne(1)).thenReturn(java.util.Optional.of(product));
        when(categoryService.readOne(2))
                .thenReturn(java.util.Optional.of(new com.projekt.kiosk.entities.CategoryEntity()));

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/products/1/edit")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                                .user("admin").roles("ADMIN"))
                        .param("name", "Updated Burger")
                        .param("priceCents", "2000")
                        .param("categoryId", "2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        org.mockito.Mockito.verify(productService).save(product);
    }

    // --- Ingredients ---

    @Test
    @DisplayName("GET /admin/ingredients returns create ingredient view")
    void createIngredientPage_returnsView() throws Exception {
        mockMvc.perform(get("/admin/ingredients")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/create-ingredient"));
    }

    @Test
    @DisplayName("POST /admin/ingredients saves ingredient and redirects")
    void createIngredient_savesIngredientAndRedirects() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/ingredients")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN"))
                .param("name", "Cheese"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        org.mockito.Mockito.verify(ingredientService)
                .save(org.mockito.ArgumentMatchers.any(com.projekt.kiosk.entities.IngredientEntity.class));
    }

    // --- Extras ---

    @Test
    @DisplayName("GET /admin/extras returns create extra view")
    void createExtraPage_returnsView() throws Exception {
        mockMvc.perform(get("/admin/extras")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/create-extra"));
    }

    @Test
    @DisplayName("POST /admin/extras saves extra and redirects")
    void createExtra_savesExtraAndRedirects() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/extras")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN"))
                .param("name", "Fries")
                .param("priceCents", "500"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        org.mockito.Mockito.verify(extraService)
                .save(org.mockito.ArgumentMatchers.any(com.projekt.kiosk.entities.ExtraEntity.class));
    }

    // --- Categories ---

    @Test
    @DisplayName("GET /admin/categories returns create category view")
    void createCategoryPage_returnsView() throws Exception {
        mockMvc.perform(get("/admin/categories")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/create-category"));
    }

    @Test
    @DisplayName("POST /admin/categories saves category and redirects")
    void createCategory_savesCategoryAndRedirects() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/categories")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN"))
                .param("name", "Burgers"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        org.mockito.Mockito.verify(categoryService)
                .save(org.mockito.ArgumentMatchers.any(com.projekt.kiosk.entities.CategoryEntity.class));
    }

    @Test
    @DisplayName("GET /admin/categories/{id}/edit returns edit view")
    void editCategoryPage_returnsView_withModel() throws Exception {
        com.projekt.kiosk.entities.CategoryEntity category = new com.projekt.kiosk.entities.CategoryEntity();
        category.setId(1);
        when(categoryService.readOne(1)).thenReturn(java.util.Optional.of(category));
        when(categoryMapper.mapTo(category)).thenReturn(new com.projekt.kiosk.dto.CategoryDto());

        mockMvc.perform(get("/admin/categories/1/edit")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/edit-category"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("category"));
    }

    @Test
    @DisplayName("POST /admin/categories/{id}/edit updates category and redirects")
    void updateCategory_updatesAndRedirects() throws Exception {
        com.projekt.kiosk.entities.CategoryEntity category = new com.projekt.kiosk.entities.CategoryEntity();
        category.setId(1);
        when(categoryService.readOne(1)).thenReturn(java.util.Optional.of(category));

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/categories/1/edit")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                                .user("admin").roles("ADMIN"))
                        .param("name", "Updated Category"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        org.mockito.Mockito.verify(categoryService).save(category);
    }

    // --- Linking ---

    @Test
    @DisplayName("GET /admin/products/{id}/ingredient returns linking view")
    void addIngredientToProductPage_returnsView() throws Exception {
        mockMvc.perform(get("/admin/products/1/ingredient")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/add-ingredient-to-product"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("productId"));
    }

    @Test
    @DisplayName("POST /admin/products/{id}/ingredient links and redirects")
    void addIngredientToProduct_linksAndRedirects() throws Exception {
        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/products/1/ingredient")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                                .user("admin").roles("ADMIN"))
                        .param("ingredientId", "2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        org.mockito.Mockito.verify(productIngredientService).create(1, 2);
    }

    @Test
    @DisplayName("GET /admin/products/{id}/extra returns linking view")
    void addExtraToProductPage_returnsView() throws Exception {
        mockMvc.perform(get("/admin/products/1/extra")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/add-extra-to-product"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("productId"));
    }

    @Test
    @DisplayName("POST /admin/products/{id}/extra links and redirects")
    void addExtraToProduct_linksAndRedirects() throws Exception {
        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/products/1/extra")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                                .user("admin").roles("ADMIN"))
                        .param("extraId", "3"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin"));

        org.mockito.Mockito.verify(productExtraService).create(1, 3);
    }
}
