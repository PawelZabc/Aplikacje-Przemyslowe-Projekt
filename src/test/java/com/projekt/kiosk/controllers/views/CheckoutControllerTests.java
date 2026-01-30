package com.projekt.kiosk.controllers.views;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.projekt.kiosk.dto.cart.Cart;

import com.projekt.kiosk.services.impl.CheckoutServiceImpl;

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
}
