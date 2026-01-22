package com.projekt.kiosk.domain;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ingredient {
    private Integer id;
    private String name;

}
