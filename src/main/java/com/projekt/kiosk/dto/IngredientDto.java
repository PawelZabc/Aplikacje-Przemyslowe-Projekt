package com.projekt.kiosk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientDto {

    private Integer id;

    @NotBlank(message = "Ingredient name is required")
    private String name;
}
