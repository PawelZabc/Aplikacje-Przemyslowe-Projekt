package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.dto.ExtraDto;
import com.projekt.kiosk.dto.IngredientDto;
import com.projekt.kiosk.dto.ProductDetailsDto;
import com.projekt.kiosk.dto.cart.Cart;
import com.projekt.kiosk.dto.cart.CartItemDto;
import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.repositories.ExtraRepository;
import com.projekt.kiosk.repositories.IngredientRepository;
import com.projekt.kiosk.repositories.ProductRepository;
import com.projekt.kiosk.services.impl.ProductDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private IngredientRepository ingredientRepository;

    @MockitoBean
    private ExtraRepository extraRepository;

    @MockitoBean
    private ProductDetailsServiceImpl productDetailsService;

    @MockitoBean
    private com.projekt.kiosk.mappers.IngredientMapper ingredientMapper;

    @MockitoBean
    private com.projekt.kiosk.mappers.ExtraMapper extraMapper;

    @BeforeEach
    void setup() {
        // Setup default mocks to avoid NPEs in mappers if called
        when(ingredientMapper.mapTo(any(IngredientEntity.class))).thenAnswer(invocation -> {
            IngredientEntity entity = invocation.getArgument(0);
            return new IngredientDto(entity.getId(), entity.getName());
        });
        when(extraMapper.mapTo(any(ExtraEntity.class))).thenAnswer(invocation -> {
            ExtraEntity entity = invocation.getArgument(0);
            return new ExtraDto(entity.getId(), entity.getName(), entity.getPriceCents());
        });
    }

    @Test
    @DisplayName("GET /cart should return cart view")
    void shouldReturnCartView() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"));
    }

    @Test
    @DisplayName("POST /cart/add should add item and redirect to cart")
    void addToCart_addsItemAndRedirects() throws Exception {
        ProductEntity product = new ProductEntity();
        product.setId(1);
        product.setName("Burger");
        product.setPriceCents(1000);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(ingredientRepository.findAllById(anyList())).thenReturn(Collections.emptyList());
        when(extraRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/cart/add")
                .param("productId", "1")
                .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @DisplayName("GET /cart/remove/{idx} should remove item and redirect")
    void removeItem_removesItemAndRedirects() throws Exception {
        Cart cart = new Cart();
        CartItemDto item = new CartItemDto();
        cart.addItem(item);

        mockMvc.perform(get("/cart/remove/0").sessionAttr("cart", cart))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @DisplayName("GET /cart/edit/{idx} should show edit view with model attributes")
    void editItem_populatesModel() throws Exception {
        Cart cart = new Cart();
        CartItemDto item = new CartItemDto();
        item.setProductId(1);
        cart.addItem(item);

        ProductDetailsDto productDetails = new ProductDetailsDto(1, "Burger", 1000, List.of(), List.of());
        when(productDetailsService.getProductDetails(1)).thenReturn(productDetails);

        mockMvc.perform(get("/cart/edit/0").sessionAttr("cart", cart))
                .andExpect(status().isOk())
                .andExpect(view().name("cart-edit"))
                .andExpect(model().attributeExists("item"))
                .andExpect(model().attribute("itemIndex", 0))
                .andExpect(model().attribute("product", productDetails));
    }

    @Test
    @DisplayName("POST /cart/edit/{idx} should update item and redirect")
    void updateItem_updatesItemAndRedirects() throws Exception {
        Cart cart = new Cart();
        CartItemDto item = new CartItemDto();
        item.setProductId(1);
        cart.addItem(item);

        when(ingredientRepository.findAllById(anyList())).thenReturn(Collections.emptyList());
        when(extraRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/cart/edit/0")
                .sessionAttr("cart", cart)
                .param("quantity", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @DisplayName("POST /cart/add handles null ingredients and extras")
    void addToCart_handlesNullIngredientsAndExtras() throws Exception {
        ProductEntity product = new ProductEntity();
        product.setId(1);
        product.setName("Burger");
        product.setPriceCents(1000);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(ingredientRepository.findAllById(anyList())).thenReturn(Collections.emptyList());
        when(extraRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/cart/add")
                .param("productId", "1")
                .param("quantity", "1")) // missing ingredients/extras params
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @DisplayName("POST /cart/edit/{idx} handles null ingredients and extras")
    void updateItem_handlesNullIngredientsAndExtras() throws Exception {
        Cart cart = new Cart();
        CartItemDto item = new CartItemDto();
        item.setProductId(1);
        item.setQuantity(1);
        cart.addItem(item);

        when(ingredientRepository.findAllById(anyList())).thenReturn(Collections.emptyList());
        when(extraRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/cart/edit/0")
                .sessionAttr("cart", cart)
                .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }
}
