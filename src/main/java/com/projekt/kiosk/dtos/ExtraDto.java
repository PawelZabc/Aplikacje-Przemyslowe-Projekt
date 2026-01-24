package com.projekt.kiosk.dtos;
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
    private String name;
    private Integer priceCents;
}
