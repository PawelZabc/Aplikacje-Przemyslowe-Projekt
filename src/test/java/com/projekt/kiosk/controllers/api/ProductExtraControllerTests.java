package com.projekt.kiosk.controllers.api;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.dto.ProductExtraDto;
import com.projekt.kiosk.repositories.ExtraRepository;
import com.projekt.kiosk.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductExtraControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final ExtraRepository extraRepository;

    @Autowired
    public ProductExtraControllerTests(
            MockMvc mockMvc,
            ProductRepository productRepository,
            ExtraRepository extraRepository) {
        this.mockMvc = mockMvc;
        this.productRepository = productRepository;
        this.extraRepository = extraRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddProductExtra() throws Exception {
        ProductEntity product = productRepository.save(TestDataUtil.createTestProductA());
        ExtraEntity extra = extraRepository.save(TestDataUtil.createTestExtraA());

        ProductExtraDto dto = ProductExtraDto.builder()
                .extraId(extra.getId())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/products/" + product.getId() + "/extras")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testGetProductExtras() throws Exception {
        ProductEntity product = productRepository.save(TestDataUtil.createTestProductA());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/products/" + product.getId() + "/extras"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
