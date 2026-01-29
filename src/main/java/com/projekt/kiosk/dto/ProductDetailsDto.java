package com.projekt.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailsDto {

    private Integer id;
    private String name;
    private int priceCents;

    private List<IngredientDto> ingredients; // removable
    private List<ExtraDto> extras;            // addable
}
