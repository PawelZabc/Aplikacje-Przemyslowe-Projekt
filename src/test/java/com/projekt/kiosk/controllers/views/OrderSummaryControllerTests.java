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

import java.util.Optional;

import com.projekt.kiosk.repositories.order.OrderRepository;
import com.projekt.kiosk.entities.order.OrderEntity;

@SpringBootTest
@AutoConfigureMockMvc
class OrderSummaryControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderRepository orderRepository;

    @Test
    @DisplayName("GET /order-summary should return order-summary view")
    void shouldReturnOrderSummaryView() throws Exception {
        OrderEntity order = new OrderEntity();
        order.setTotalPriceCents(1000);
        when(orderRepository.findByOrderNumber("123")).thenReturn(Optional.of(order));

        mockMvc.perform(get("/order/summary/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order-summary"));
    }
}
