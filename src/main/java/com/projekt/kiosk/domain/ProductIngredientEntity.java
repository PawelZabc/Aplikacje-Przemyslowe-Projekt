package com.projekt.kiosk.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductIngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_ingredient_seq")
    @SequenceGenerator(
            name = "product_ingredient_seq",
            sequenceName = "product_ingredient_seq",
            allocationSize = 1
    )
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;
}
