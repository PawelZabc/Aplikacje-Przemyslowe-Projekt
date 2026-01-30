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
import static org.mockito.Mockito.when;

import com.projekt.kiosk.services.impl.ProductDetailsServiceImpl;
import com.projekt.kiosk.dto.ProductDetailsDto;

@SpringBootTest
@AutoConfigureMockMvc
class ProductConfiguratorControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductDetailsServiceImpl productDetailsService;

    @Test
    @DisplayName("GET /product-configurator should return product-configurator view")
    void shouldReturnProductConfiguratorView() throws Exception {
        when(productDetailsService.getProductDetails(1)).thenReturn(new ProductDetailsDto());

        mockMvc.perform(get("/product/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("product-configurator"));
    }
}
