package com.projekt.kiosk;

import com.projekt.kiosk.domain.Ingredient;

public final class TestDataUtil {
    public static Ingredient createTestIngredientA() {
        return Ingredient.builder()
                .id(1)
                .name("Cheese")
                .build();
    }

    public static Ingredient createTestIngredientB() {
        return Ingredient.builder()
                .id(2)
                .name("Tomato")
                .build();
    }

    public static Ingredient createTestIngredientC() {
        return Ingredient.builder()
                .id(3)
                .name("Lettuce")
                .build();
    }

    public static Ingredient createTestIngredientD() {
        return Ingredient.builder()
                .id(4)
                .name("Onion")
                .build();
    }

}
