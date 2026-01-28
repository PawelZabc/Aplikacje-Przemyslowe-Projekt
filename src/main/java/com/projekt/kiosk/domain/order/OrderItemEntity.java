package com.projekt.kiosk.domain.order;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq")
    @SequenceGenerator(
            name = "order_item_seq",
            sequenceName = "order_item_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id",nullable = false)
    private OrderEntity order;

    @Column(name="product_name", nullable = false)
    private String productName;

    @Column(name="base_price_cents", nullable = false)
    private Integer basePriceCents;

    @Column(nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
    private List<OrderItemIngredientEntity> removedIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL)
    private List<OrderItemExtraEntity> addedExtras = new ArrayList<>();
}