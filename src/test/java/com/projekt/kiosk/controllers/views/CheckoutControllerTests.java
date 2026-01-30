package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.dto.cart.Cart;
import com.projekt.kiosk.dto.cart.CartItemDto;
import com.projekt.kiosk.entities.order.OrderEntity;
import com.projekt.kiosk.services.impl.CheckoutServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CheckoutServiceImpl checkoutService;

    @Test
    @DisplayName("POST /checkout with empty cart should redirect to /cart")
    void shouldRedirectWhenCartEmpty() throws Exception {
        Cart cart = new Cart();

        mockMvc.perform(post("/checkout").sessionAttr("cart", cart))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/cart"));
    }

    @Test
    @DisplayName("POST /checkout with items should create order and redirect to summary")
    void checkout_success_redirectsToSummary() throws Exception {
        Cart cart = new Cart();
        cart.addItem(new CartItemDto());

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-123");
        when(checkoutService.checkout(any(Cart.class))).thenReturn(order);

        mockMvc.perform(post("/checkout").sessionAttr("cart", cart))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/order/summary/ORD-123"));
    }
}
