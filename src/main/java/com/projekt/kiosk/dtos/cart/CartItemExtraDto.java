package com.projekt.kiosk.dtos.cart;

import lombok.Data;

@Data
public class CartItemExtraDto {
    private Integer extraId;
    private String name;
    private Integer priceCents;
}
