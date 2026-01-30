package com.projekt.kiosk.dto.cart;

import com.projekt.kiosk.enums.OrderType;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<CartItemDto> items = new ArrayList<>();
    private OrderType orderType = OrderType.DINE_IN;

    public List<CartItemDto> getItems() {
        return items;
    }

    public CartItemDto getItem(int index) {
        validateIndex(index);
        return items.get(index);
    }

    public void removeItem(int index) {
        validateIndex(index);
        items.remove(index);
    }

    public void addItem(CartItemDto item) {
        items.add(item);
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType != null ? orderType : OrderType.DINE_IN;
    }

    /**
     * Get subtotal of all items (before packaging fee).
     */
    public int getSubtotalCents() {
        return items.stream()
                .mapToInt(CartItemDto::getItemTotalCents)
                .sum();
    }

    /**
     * Get packaging fee based on order type.
     */
    public int getPackagingFeeCents() {
        return orderType != null ? orderType.getPackagingFeeCents() : 0;
    }

    /**
     * Get total price including packaging fee.
     */
    public int getTotalPrice() {
        return getSubtotalCents() + getPackagingFeeCents();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
        orderType = OrderType.DINE_IN;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Invalid cart item index: " + index);
        }
    }
}
