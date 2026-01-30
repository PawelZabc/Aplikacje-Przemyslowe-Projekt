package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.entities.CategoryEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.services.ProductService;
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

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("GET /menu should return menu view and products")
    void shouldReturnMenuView() throws Exception {
        CategoryEntity category = new CategoryEntity();
        category.setName("Burgers");

        ProductEntity product = new ProductEntity();
        product.setCategory(category);
        product.setName("Cheeseburger");
        product.setPriceCents(1000);

        when(productService.readAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/menu"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("menu"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("productsByCategory"));
    }
}
