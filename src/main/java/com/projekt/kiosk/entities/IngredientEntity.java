package com.projekt.kiosk.entities;


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
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
//    @SequenceGenerator(
//            name = "ingredient_seq",
//            sequenceName = "ingredient_seq",
//            allocationSize = 1
//    )
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

}
