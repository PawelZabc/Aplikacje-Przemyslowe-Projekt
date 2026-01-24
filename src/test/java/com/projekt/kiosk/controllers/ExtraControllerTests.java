package com.projekt.kiosk.controllers;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.domain.ExtraEntity;
import com.projekt.kiosk.dtos.ExtraDto;
import com.projekt.kiosk.services.ExtraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ExtraControllerTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ExtraService extraService;

    @Autowired
    public ExtraControllerTests(MockMvc mockMvc, ExtraService extraService) {
        this.mockMvc = mockMvc;
        this.extraService = extraService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateExtraCorrectCode() throws Exception {
        ExtraDto extra = ExtraDto.builder()
                .name("Cheese")
                .priceCents(150)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/extras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(extra)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testCreateExtraCorrectReturnValue() throws Exception {
        ExtraDto extra = ExtraDto.builder()
                .name("Bacon")
                .priceCents(200)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/extras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(extra)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(extra.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(extra.getPriceCents()));
    }

    @Test
    public void testReadExtrasCorrectCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/extras"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testReadExtrasCorrectReturnValue() throws Exception {
        ExtraEntity extra = TestDataUtil.createTestExtraA();
        extraService.save(extra);

        mockMvc.perform(MockMvcRequestBuilders.get("/extras"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(extra.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].priceCents").value(extra.getPriceCents()));
    }

    @Test
    public void testReadOneExtraCorrectReturnValue() throws Exception {
        ExtraEntity extra = TestDataUtil.createTestExtraA();
        extraService.save(extra);

        mockMvc.perform(MockMvcRequestBuilders.get("/extras/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(extra.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(extra.getPriceCents()));
    }

    @Test
    public void testReadOneExtraNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/extras/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateExtraCorrectCodeOk() throws Exception {
        ExtraEntity extraA = TestDataUtil.createTestExtraA();
        extraService.save(extraA);

        ExtraDto extraB = ExtraDto.builder()
                .name("Updated Extra")
                .priceCents(300)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/extras/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(extraB)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateExtraCorrectValue() throws Exception {
        ExtraEntity extraA = TestDataUtil.createTestExtraA();
        extraService.save(extraA);

        ExtraDto extraB = ExtraDto.builder()
                .name("Updated Extra")
                .priceCents(300)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/extras/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(extraB)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(extraA.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(extraB.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(extraB.getPriceCents()));
    }

    @Test
    public void testPatchExtraCorrectValue() throws Exception {
        ExtraEntity extraA = TestDataUtil.createTestExtraA();
        extraService.save(extraA);

        ExtraDto patch = ExtraDto.builder()
                .priceCents(999)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/extras/" + extraA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(extraA.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(999));
    }

    @Test
    public void testDeleteExtraCorrectCodeNoContent() throws Exception {
        ExtraEntity extra = TestDataUtil.createTestExtraA();
        extraService.save(extra);

        mockMvc.perform(MockMvcRequestBuilders.delete("/extras/" + extra.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}

