package com.projekt.kiosk.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.dto.ExtraDto;
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
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
@AutoConfigureMockMvc
public class ExtraControllerTests {

        private static final String BASE_URL = "/api/v1/extras";

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ExtraService extraService;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Test
        public void testCreateExtraCorrectCode() throws Exception {
                ExtraDto extra = ExtraDto.builder()
                                .name("Cheese")
                                .priceCents(150)
                                .build();

                mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                                .with(user("admin").roles("ADMIN"))
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

                mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(extra)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(extra.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(extra.getPriceCents()));
        }

        @Test
        public void testReadExtrasCorrectCode() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        public void testReadExtrasCorrectReturnValue() throws Exception {
                ExtraEntity extra = TestDataUtil.createTestExtraA();
                extraService.save(extra);

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name")
                                                .value(org.hamcrest.Matchers.hasItem(extra.getName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[*].priceCents")
                                                .value(org.hamcrest.Matchers.hasItem(extra.getPriceCents())));
        }

        @Test
        public void testReadOneExtraCorrectReturnValue() throws Exception {
                ExtraEntity extra = TestDataUtil.createTestExtraA();
                extraService.save(extra);

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(extra.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(extra.getPriceCents()));
        }

        @Test
        public void testReadOneExtraNotFound() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/99999"))
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

                mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + extraA.getId())
                                .with(user("admin").roles("ADMIN"))
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

                mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + extraA.getId())
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(extraB)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(extraA.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(extraB.getName()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents")
                                                .value(extraB.getPriceCents()));
        }

        @Test
        public void testPatchExtraCorrectValue() throws Exception {
                ExtraEntity extraA = TestDataUtil.createTestExtraA();
                extraService.save(extraA);

                ExtraDto patch = ExtraDto.builder()
                                .priceCents(999)
                                .build();

                mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/" + extraA.getId())
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patch)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(extraA.getId()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.priceCents").value(999));
        }

        @Test
        public void testDeleteExtraCorrectCodeNoContent() throws Exception {
                ExtraEntity extra = TestDataUtil.createTestExtraA();
                extraService.save(extra);

                mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + extra.getId())
                                .with(user("admin").roles("ADMIN")))
                                .andExpect(MockMvcResultMatchers.status().isNoContent());
        }
}
