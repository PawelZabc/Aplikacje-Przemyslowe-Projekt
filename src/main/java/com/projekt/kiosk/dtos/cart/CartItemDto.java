package com.projekt.kiosk.dtos.cart;

import com.projekt.kiosk.dtos.ExtraDto;
import com.projekt.kiosk.dtos.IngredientDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private Integer productId;
    private String productName;
    private Integer basePriceCents;
    private int quantity;

    // REMOVED ingredients
    private List<IngredientDto> removedIngredients = new ArrayList<>();

    // ADDED extras
    private List<ExtraDto> addedExtras = new ArrayList<>();

    public int getItemTotalCents() {
        int extrasTotal = addedExtras.stream()
                .mapToInt(ExtraDto::getPriceCents)
                .sum();

        return (basePriceCents + extrasTotal) * quantity;
    }
}

