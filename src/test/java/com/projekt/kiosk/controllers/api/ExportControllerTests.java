package com.projekt.kiosk.controllers.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

/**
 * Tests for ExportController CSV/PDF export endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WithMockUser(roles = "ADMIN")
public class ExportControllerTests {

        private static final String BASE_URL = "/api/v1/export";

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Test
        public void testExportStatsCsvReturnsOk() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/stats/csv"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.header().exists("Content-Disposition"))
                                .andExpect(MockMvcResultMatchers.content().contentType("text/csv"));
        }

        @Test
        public void testExportStatsCsvWithDateRange() throws Exception {
                LocalDate from = LocalDate.now().minusDays(7);
                LocalDate to = LocalDate.now();

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/stats/csv")
                                .param("from", from.toString())
                                .param("to", to.toString()))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition",
                                                org.hamcrest.Matchers.containsString(".csv")));
        }

        @Test
        public void testExportStatsCsvContainsHeader() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/stats/csv"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().string(
                                                org.hamcrest.Matchers.containsString("Date,Order Count,Revenue")));
        }

        @Test
        public void testExportStatsCsvWithData() throws Exception {
                // Insert test order
                jdbcTemplate.update(
                                "INSERT INTO orders (id, order_number, created_at, total_price_cents, order_type, packaging_fee_cents) "
                                                +
                                                "VALUES (nextval('order_seq'), ?, NOW(), ?, 'DINE_IN', 0)",
                                "EXPORT001", 2500);

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/stats/csv"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().string(
                                                org.hamcrest.Matchers.containsString("25.00")));
        }

        @Test
        public void testExportStatsPdfReturnsOk() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/stats/pdf"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.header().exists("Content-Disposition"));
        }

        @Test
        public void testExportStatsPdfContainsReportTitle() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/stats/pdf"))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().string(
                                                org.hamcrest.Matchers.containsString("%PDF")));
        }
}
