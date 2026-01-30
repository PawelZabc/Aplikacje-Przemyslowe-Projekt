package com.projekt.kiosk.dto.cart;

import com.projekt.kiosk.dto.ExtraDto;
import com.projekt.kiosk.enums.OrderType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class CartTests {

    @Test
    public void testEmptyCart() {
        Cart cart = new Cart();

        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getTotalPrice()).isEqualTo(0);
    }

    @Test
    public void testDefaultOrderTypeIsDineIn() {
        Cart cart = new Cart();

        assertThat(cart.getOrderType()).isEqualTo(OrderType.DINE_IN);
        assertThat(cart.getPackagingFeeCents()).isEqualTo(0);
    }

    @Test
    public void testTakeawayHasPackagingFee() {
        Cart cart = new Cart();
        cart.setOrderType(OrderType.TAKEAWAY);

        assertThat(cart.getOrderType()).isEqualTo(OrderType.TAKEAWAY);
        assertThat(cart.getPackagingFeeCents()).isEqualTo(100);
    }

    @Test
    public void testAddItemToCart() {
        Cart cart = new Cart();
        CartItemDto item = createTestItem("Burger", 500, 1);

        cart.addItem(item);

        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getSubtotalCents()).isEqualTo(500);
    }

    @Test
    public void testTotalPriceWithDineIn() {
        Cart cart = new Cart();
        cart.setOrderType(OrderType.DINE_IN);
        cart.addItem(createTestItem("Burger", 500, 2));

        assertThat(cart.getSubtotalCents()).isEqualTo(1000);
        assertThat(cart.getPackagingFeeCents()).isEqualTo(0);
        assertThat(cart.getTotalPrice()).isEqualTo(1000);
    }

    @Test
    public void testTotalPriceWithTakeaway() {
        Cart cart = new Cart();
        cart.setOrderType(OrderType.TAKEAWAY);
        cart.addItem(createTestItem("Burger", 500, 2));

        assertThat(cart.getSubtotalCents()).isEqualTo(1000);
        assertThat(cart.getPackagingFeeCents()).isEqualTo(100);
        assertThat(cart.getTotalPrice()).isEqualTo(1100);
    }

    @Test
    public void testCartWithExtras() {
        Cart cart = new Cart();
        cart.setOrderType(OrderType.DINE_IN);

        CartItemDto item = new CartItemDto();
        item.setProductId(1);
        item.setProductName("Burger");
        item.setBasePriceCents(500);
        item.setQuantity(1);
        item.getAddedExtras().add(ExtraDto.builder()
                .id(1)
                .name("Bacon")
                .priceCents(150)
                .build());

        cart.addItem(item);

        assertThat(cart.getSubtotalCents()).isEqualTo(650);
        assertThat(cart.getTotalPrice()).isEqualTo(650);
    }

    @Test
    public void testCartWithExtrasAndTakeaway() {
        Cart cart = new Cart();
        cart.setOrderType(OrderType.TAKEAWAY);

        CartItemDto item = new CartItemDto();
        item.setProductId(1);
        item.setProductName("Burger");
        item.setBasePriceCents(500);
        item.setQuantity(2);
        item.getAddedExtras().add(ExtraDto.builder()
                .id(1)
                .name("Bacon")
                .priceCents(150)
                .build());

        cart.addItem(item);

        assertThat(cart.getSubtotalCents()).isEqualTo(1300);
        assertThat(cart.getTotalPrice()).isEqualTo(1400);
    }

    @Test
    public void testRemoveItem() {
        Cart cart = new Cart();
        cart.addItem(createTestItem("Burger", 500, 1));
        cart.addItem(createTestItem("Fries", 200, 1));

        assertThat(cart.getItems()).hasSize(2);

        cart.removeItem(0);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getProductName()).isEqualTo("Fries");
    }

    @Test
    public void testClearCart() {
        Cart cart = new Cart();
        cart.setOrderType(OrderType.TAKEAWAY);
        cart.addItem(createTestItem("Burger", 500, 1));

        cart.clear();

        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.getOrderType()).isEqualTo(OrderType.DINE_IN);
    }

    private CartItemDto createTestItem(String name, int priceCents, int quantity) {
        CartItemDto item = new CartItemDto();
        item.setProductId(1);
        item.setProductName(name);
        item.setBasePriceCents(priceCents);
        item.setQuantity(quantity);
        return item;
    }
}
