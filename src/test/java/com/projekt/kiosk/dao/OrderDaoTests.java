package com.projekt.kiosk.dao;

import com.projekt.kiosk.dto.ExtraDto;
import com.projekt.kiosk.dto.IngredientDto;
import com.projekt.kiosk.dto.cart.Cart;
import com.projekt.kiosk.dto.cart.CartItemDto;
import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.entities.order.OrderEntity;
import com.projekt.kiosk.enums.OrderType;
import com.projekt.kiosk.mappers.rowmappers.ExtraRowMapper;
import com.projekt.kiosk.mappers.rowmappers.IngredientRowMapper;
import com.projekt.kiosk.mappers.rowmappers.ProductRowMapper;
import com.projekt.kiosk.repositories.order.OrderNumberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderDaoTests {

        @Mock
        private JdbcTemplate jdbcTemplate;

        @Mock
        private OrderNumberGenerator orderNumberGenerator;

        @InjectMocks
        private OrderDaoImpl orderDao;

        @Test
        @DisplayName("saveOrderSnapshot saves order with items, ingredients, and extras")
        void saveOrderSnapshot_savesOrderWithItems() {
                // --- DATA SETUP ---
                ProductEntity product = new ProductEntity();
                product.setId(1);
                product.setName("Burger");
                product.setPriceCents(1000);

                IngredientEntity ingredient = new IngredientEntity();
                ingredient.setId(10);
                ingredient.setName("Onion");

                ExtraEntity extra = new ExtraEntity();
                extra.setId(20);
                extra.setName("Cheese");
                extra.setPriceCents(200);

                Cart cart = new Cart();
                cart.setOrderType(OrderType.TAKEAWAY); // Packaging fee 100

                CartItemDto item = new CartItemDto();
                item.setProductId(1);
                item.setQuantity(2);
                item.setBasePriceCents(1000);
                item.setRemovedIngredients(List.of(new IngredientDto(10, "Onion")));
                item.setAddedExtras(List.of(new ExtraDto(20, "Cheese", 200)));
                cart.addItem(item);

                // --- MOCKS ---
                when(orderNumberGenerator.generateOrderNumber()).thenReturn("ORD-001");

                // 1. Mock Insert Order returning ID
                when(jdbcTemplate.query(
                                contains("INSERT INTO orders"),
                                any(RowMapper.class),
                                any(), any(), any(), any(), any() // arguments
                )).thenReturn(List.of(100L));

                // 2. Mock Product Lookup
                when(jdbcTemplate.query(
                                contains("SELECT id, name, price_cents FROM products"),
                                any(ProductRowMapper.class),
                                eq(1))).thenReturn(List.of(product));

                // 3. Mock Insert Order Item returning ID
                when(jdbcTemplate.query(
                                contains("INSERT INTO order_items"),
                                any(RowMapper.class),
                                any(), any(), any(), any())).thenReturn(List.of(200L));

                // 4. Mock Ingredient Lookup
                when(jdbcTemplate.query(
                                contains("SELECT i.id, i.name FROM ingredients"),
                                any(IngredientRowMapper.class),
                                eq(1), eq(10))).thenReturn(List.of(ingredient));

                // 5. Mock Extra Lookup
                when(jdbcTemplate.query(
                                contains("SELECT e.id, e.name, e.price_cents FROM extras"),
                                any(ExtraRowMapper.class),
                                eq(1), eq(20))).thenReturn(List.of(extra));

                // 6. Mock Updates (ingredients, extras, total update)
                // Ingredients: 2 args (order_item_id, ingredient_name)
                lenient().when(jdbcTemplate.update(contains("INSERT INTO order_item_ingredients"), any(), any()))
                                .thenReturn(1);

                // Extras: 3 args (order_item_id, extra_name, price_cents)
                lenient().when(jdbcTemplate.update(contains("INSERT INTO order_item_extras"), any(), any(), any()))
                                .thenReturn(1);

                // Final update: 2 args (total_price, order_id)
                lenient().when(jdbcTemplate.update(contains("UPDATE orders"), any(), any())).thenReturn(1);

                // --- EXECUTE ---
                OrderEntity order = orderDao.saveOrderSnapshot(cart);

                // --- VERIFY ---
                assertThat(order).isNotNull();
                assertThat(order.getOrderNumber()).isEqualTo("ORD-001");
                // (1000 + 200) * 2 + 100 = 2500
                assertThat(order.getTotalPriceCents()).isEqualTo(2500);
                assertThat(order.getOrderType()).isEqualTo(OrderType.TAKEAWAY);

                // Verify interactions
                verify(jdbcTemplate, times(1)).query(
                                contains("INSERT INTO orders"), any(RowMapper.class), any(), any(), any(), any(),
                                any());

                // Verify we updated the total price at the end
                verify(jdbcTemplate).update(
                                contains("UPDATE orders SET total_price_cents"),
                                eq(2500),
                                eq(100L));
        }

        @Test
        @DisplayName("saveOrderSnapshot throws exception on invalid product")
        void saveOrderSnapshot_throwsOnInvalidProduct() {
                Cart cart = new Cart();
                CartItemDto item = new CartItemDto();
                item.setProductId(999);
                item.setQuantity(1);
                item.setBasePriceCents(100);
                cart.addItem(item);

                when(orderNumberGenerator.generateOrderNumber()).thenReturn("ORD-001");
                when(jdbcTemplate.query(
                                contains("INSERT INTO orders"),
                                any(RowMapper.class),
                                any(), any(), any(), any(), any())).thenReturn(List.of(100L));

                when(jdbcTemplate.query(
                                contains("SELECT id, name, price_cents FROM products"),
                                any(ProductRowMapper.class),
                                eq(999))).thenReturn(Collections.emptyList()); // Not found

                assertThatThrownBy(() -> orderDao.saveOrderSnapshot(cart))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("Invalid productId");
        }

        @Test
        @DisplayName("saveOrderSnapshot throws exception on price mismatch")
        void saveOrderSnapshot_throwsOnPriceMismatch() {
                ProductEntity product = new ProductEntity();
                product.setId(1);
                product.setName("Burger");
                product.setPriceCents(1000);

                Cart cart = new Cart();
                CartItemDto item = new CartItemDto();
                item.setProductId(1);
                item.setQuantity(1);
                item.setBasePriceCents(500); // Mismatch!
                cart.addItem(item);

                when(orderNumberGenerator.generateOrderNumber()).thenReturn("ORD-001");
                when(jdbcTemplate.query(
                                contains("INSERT INTO orders"),
                                any(RowMapper.class),
                                any(), any(), any(), any(), any())).thenReturn(List.of(100L));

                when(jdbcTemplate.query(
                                contains("SELECT id, name, price_cents FROM products"),
                                any(ProductRowMapper.class),
                                eq(1))).thenReturn(List.of(product));

                assertThatThrownBy(() -> orderDao.saveOrderSnapshot(cart))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("Invalid basePriceCents");
        }
}
