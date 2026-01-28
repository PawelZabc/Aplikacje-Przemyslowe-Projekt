package com.projekt.kiosk.domain.order;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_item_ingredients")
public class OrderItemIngredientEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_item_ingredient_seq"
    )
    @SequenceGenerator(
            name = "order_item_ingredient_seq",
            sequenceName = "order_item_ingredient_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItemEntity orderItem;

    @Column(name="ingredient_name", nullable = false)
    private String ingredientName;
}
