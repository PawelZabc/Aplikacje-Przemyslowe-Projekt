package com.projekt.kiosk.dtos.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<CartItemDto> items = new ArrayList<>();

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

    public int getTotalPrice() {
        return items.stream()
                .mapToInt(CartItemDto::getItemTotalCents)
                .sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Invalid cart item index: " + index);
        }
    }
}



