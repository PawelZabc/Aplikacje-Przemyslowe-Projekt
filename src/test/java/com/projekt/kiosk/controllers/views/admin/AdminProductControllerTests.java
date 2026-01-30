package com.projekt.kiosk.controllers.views.admin;

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

import com.projekt.kiosk.controllers.views.AdminController;
import com.projekt.kiosk.services.ProductService;
import com.projekt.kiosk.services.IngredientService;
import com.projekt.kiosk.services.ExtraService;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.impl.ProductIngredientServiceImpl;
import com.projekt.kiosk.services.impl.ProductExtraServiceImpl;
import com.projekt.kiosk.mappers.ExtraMapper;
import com.projekt.kiosk.mappers.IngredientMapper;
import com.projekt.kiosk.KioskApplication;

@SpringBootTest(classes = KioskApplication.class)
@AutoConfigureMockMvc
class AdminProductControllerTests {
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
    private ExtraMapper extraMapper;

    @MockitoBean
    private IngredientMapper ingredientMapper;

    @Test
    @DisplayName("GET /admin/products should return admin products view")
    void shouldReturnAdminProductsView() throws Exception {
        when(productService.readAll()).thenReturn(List.of());
        when(extraService.readAll()).thenReturn(List.of());
        when(ingredientService.readAll()).thenReturn(List.of());

        mockMvc.perform(get("/admin/products")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
                        .user("admin").roles("ADMIN")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/create-product"));
    }
}
