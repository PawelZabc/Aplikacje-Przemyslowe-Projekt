package com.projekt.kiosk.dtos;

import java.util.List;

public class ProductDetailsDto {

    private Integer id;
    private String name;
    private int priceCents;

    private List<IngredientDto> ingredients; // removable
    private List<ExtraDto> extras;            // addable
}
