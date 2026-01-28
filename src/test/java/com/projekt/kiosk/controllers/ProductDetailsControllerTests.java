//package com.projekt.kiosk.controllers;
//
//import com.projekt.kiosk.dtos.ExtraDto;
//import com.projekt.kiosk.dtos.IngredientDto;
//import com.projekt.kiosk.dtos.ProductDetailsDto;
//import com.projekt.kiosk.exceptions.ResourceNotFoundException;
//import com.projekt.kiosk.services.inpl.ProductDetailsService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//
//@WebMvcTest(controllers = ProductDetailsController.class)
//class ProductDetailsControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProductDetailsService productDetailsService;
//
//    @Test
//    void getProductDetails_returns200AndBody() throws Exception {
//        ProductDetailsDto dto = new ProductDetailsDto(
//                1,
//                "Burger",
//                1500,
//                List.of(new IngredientDto(10, "Cheese")),
//                List.of(new ExtraDto(20, "Bacon", 300))
//        );
//
//        when(productDetailsService.getProductDetails(1)).thenReturn(dto);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/products/details/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Burger"))
//                .andExpect(jsonPath("$.priceCents").value(1500))
//                .andExpect(jsonPath("$.ingredients[0].name").value("Cheese"))
//                .andExpect(jsonPath("$.extras[0].name").value("Bacon"))
//                .andExpect(jsonPath("$.extras[0].priceCents").value(300));
//    }
//
//    @Test
//    void getProductDetails_returns404_whenNotFound() throws Exception {
//        when(productDetailsService.getProductDetails(1))
//                .thenThrow(new ResourceNotFoundException("Product not found"));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/products/details/1"))
//                .andExpect(status().isNotFound());
//    }
//}
