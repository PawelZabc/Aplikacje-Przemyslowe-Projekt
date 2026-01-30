package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.dto.ProductDto;
import com.projekt.kiosk.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WithMockUser(roles = "ADMIN")
public class ProductControllerTests {

        private static final String BASE_URL = "/api/v1/products";

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
        @WithMockUser(roles = "ADMIN")
        public void testCreateProductCorrectCode() throws Exception {
                ProductDto product = ProductDto.builder()
                                .name("Burger")
                                .priceCents(500)
                                .build();

                mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                                .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void testCreateProductCorrectReturnValue() throws Exception {
                ProductDto product = ProductDto.builder()
                                .name("Pizza")
                                .priceCents(800)
                                .build();

                mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents")
                                                .value(product.getPriceCents()));
        }

        @Test
        public void testReadProductsCorrectCode() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        public void testReadProductsCorrectReturnValue() throws Exception {
                ProductEntity product = TestDataUtil.createTestProductA();
                productService.save(product);

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(product.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].priceCents")
                                                .value(product.getPriceCents()));
        }

        @Test
        public void testReadOneProductCorrectReturnValue() throws Exception {
                ProductEntity product = TestDataUtil.createTestProductA();
                productService.save(product);

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents")
                                                .value(product.getPriceCents()));
        }

        @Test
        public void testReadOneProductNotFound() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/999"))
                                .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void testUpdateProductCorrectCodeOk() throws Exception {
                ProductEntity productA = TestDataUtil.createTestProductA();
                productService.save(productA);

                ProductDto productB = ProductDto.builder()
                                .name("Updated Product")
                                .priceCents(1000)
                                .build();

                mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + productA.getId())
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productB)))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void testUpdateProductCorrectReturnValue() throws Exception {
                ProductEntity productA = TestDataUtil.createTestProductA();
                productService.save(productA);

                ProductDto productB = ProductDto.builder()
                                .name("Updated Product")
                                .priceCents(1000)
                                .build();

                mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + productA.getId())
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productB)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productA.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(productB.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents")
                                                .value(productB.getPriceCents()));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void testPatchProductCorrectValue() throws Exception {
                ProductEntity productA = TestDataUtil.createTestProductA();
                productService.save(productA);

                ProductDto patch = ProductDto.builder()
                                .priceCents(1200)
                                .build();

                mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/" + productA.getId())
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patch)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(productA.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(1200));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void testDeleteProductCorrectCodeNoContent() throws Exception {
                ProductEntity productA = TestDataUtil.createTestProductA();
                productService.save(productA);

                mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + productA.getId())
                                .with(user("admin").roles("ADMIN")))
                                .andExpect(MockMvcResultMatchers.status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void testDeleteNotExistingProductReturnsNotFound() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/9999")
                                .with(user("admin").roles("ADMIN")))
                                .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        // Bean Validation Tests
        @Test
        @WithMockUser(roles = "ADMIN")
        public void testCreateProductWithEmptyNameReturnsBadRequest() throws Exception {
                ProductDto product = ProductDto.builder()
                                .name("")
                                .priceCents(500)
                                .build();

                mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void testCreateProductWithNullNameReturnsBadRequest() throws Exception {
                ProductDto product = ProductDto.builder()
                                .priceCents(500)
                                .build();

                mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void testCreateProductWithNegativePriceReturnsBadRequest() throws Exception {
                ProductDto product = ProductDto.builder()
                                .name("Test Product")
                                .priceCents(-100)
                                .build();

                mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
}
