package com.projekt.kiosk.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExtraDto {

    private Integer id;

    @NotBlank(message = "Extra name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    private Integer priceCents;
}
