package com.projekt.kiosk.domain;


import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ingredients")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE, generator = "ingredient_seq")
    @SequenceGenerator(
            name = "ingredient_seq",
            sequenceName = "ingredient_seq",
            allocationSize = 1
    )
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

}
