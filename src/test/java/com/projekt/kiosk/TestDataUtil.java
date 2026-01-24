package com.projekt.kiosk;

import com.projekt.kiosk.domain.IngredientEntity;

public final class TestDataUtil {
    public static IngredientEntity createTestIngredientA() {
        return IngredientEntity.builder()
                .name("Cheese")
                .build();
    }

    public static IngredientEntity createTestIngredientB() {
        return IngredientEntity.builder()
                .name("Tomato")
                .build();
    }

    public static IngredientEntity createTestIngredientC() {
        return IngredientEntity.builder()
                .name("Lettuce")
                .build();
    }

    public static IngredientEntity createTestIngredientD() {
        return IngredientEntity.builder()
                .name("Onion")
                .build();
    }

}
