package com.projekt.kiosk.controllers.views;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private com.projekt.kiosk.repositories.ProductRepository productRepository;

    @MockitoBean
    private com.projekt.kiosk.repositories.IngredientRepository ingredientRepository;

    @MockitoBean
    private com.projekt.kiosk.repositories.ExtraRepository extraRepository;

    @MockitoBean
    private com.projekt.kiosk.services.impl.ProductDetailsServiceImpl productDetailsService;

    @MockitoBean
    private com.projekt.kiosk.mappers.IngredientMapper ingredientMapper;

    @MockitoBean
    private com.projekt.kiosk.mappers.ExtraMapper extraMapper;

    @Test
    @DisplayName("GET /cart should return cart view")
    void shouldReturnCartView() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cart"));
    }
}
