package com.projekt.kiosk;

import com.projekt.kiosk.domain.Ingredient;

public final class TestDataUtil {
    public static Ingredient createTestIngredientA() {
        return Ingredient.builder()
                .name("Cheese")
                .build();
    }

    public static Ingredient createTestIngredientB() {
        return Ingredient.builder()
                .name("Tomato")
                .build();
    }

    public static Ingredient createTestIngredientC() {
        return Ingredient.builder()
                .name("Lettuce")
                .build();
    }

    public static Ingredient createTestIngredientD() {
        return Ingredient.builder()
                .name("Onion")
                .build();
    }

}
