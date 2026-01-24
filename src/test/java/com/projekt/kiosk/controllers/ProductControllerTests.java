package com.projekt.kiosk.controllers;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.domain.ProductEntity;
import com.projekt.kiosk.dtos.ProductDto;
import com.projekt.kiosk.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ProductService productService;

    @Autowired
    public ProductControllerTests(MockMvc mockMvc, ProductService productService) {
        this.mockMvc = mockMvc;
        this.productService = productService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateProductCorrectCode() throws Exception {
        ProductDto product = ProductDto.builder()
                .name("Burger")
                .priceCents(500)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testCreateProductCorrectReturnValue() throws Exception {
        ProductDto product = ProductDto.builder()
                .name("Pizza")
                .priceCents(800)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(product.getPriceCents()));
    }

    @Test
    public void testReadProductsCorrectCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testReadProductsCorrectReturnValue() throws Exception {
        ProductEntity product = TestDataUtil.createTestProductA();
        productService.save(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(product.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].priceCents").value(product.getPriceCents()));
    }

    @Test
    public void testReadOneProductCorrectReturnValue() throws Exception {
        ProductEntity product = TestDataUtil.createTestProductA();
        productService.save(product);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(product.getPriceCents()));
    }

    @Test
    public void testReadOneProductNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateProductCorrectCodeOk() throws Exception {
        ProductEntity productA = TestDataUtil.createTestProductA();
        productService.save(productA);

        ProductDto productB = ProductDto.builder()
                .name("Updated Product")
                .priceCents(1000)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productB)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateProductCorrectReturnValue() throws Exception {
        ProductEntity productA = TestDataUtil.createTestProductA();
        productService.save(productA);

        ProductDto productB = ProductDto.builder()
                .name("Updated Product")
                .priceCents(1000)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productB)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productA.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(productB.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(productB.getPriceCents()));
    }

    @Test
    public void testPatchProductCorrectValue() throws Exception {
        ProductEntity productA = TestDataUtil.createTestProductA();
        productService.save(productA);

        ProductDto patch = ProductDto.builder()
                .priceCents(1200)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/products/" + productA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productA.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(1200));
    }

    @Test
    public void testDeleteProductCorrectCodeNoContent() throws Exception {
        ProductEntity productA = TestDataUtil.createTestProductA();
        productService.save(productA);

        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + productA.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteNotExistingProductCorrectCodeNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/9999"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
