package com.projekt.kiosk;

import com.projekt.kiosk.domain.ExtraEntity;
import com.projekt.kiosk.domain.IngredientEntity;
import com.projekt.kiosk.domain.ProductEntity;

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

    public static ExtraEntity createTestExtraA() {
        return ExtraEntity.builder()
                .name("Cheese")
                .priceCents(100)
                .build();
    }

    public static ExtraEntity createTestExtraB() {
        return ExtraEntity.builder()
                .name("Bacon")
                .priceCents(200)
                .build();
    }

    public static ExtraEntity createTestExtraC() {
        return ExtraEntity.builder()
                .name("Avocado")
                .priceCents(300)
                .build();
    }

    public static ProductEntity createTestProductA() {
        return ProductEntity.builder()
                .name("Burger")
                .priceCents(500)
                .build();
    }

    public static ProductEntity createTestProductB() {
        return ProductEntity.builder()
                .name("Pizza")
                .priceCents(800)
                .build();
    }

    public static ProductEntity createTestProductC() {
        return ProductEntity.builder()
                .name("Pasta")
                .priceCents(650)
                .build();
    }


}
