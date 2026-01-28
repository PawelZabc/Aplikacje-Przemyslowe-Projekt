package com.projekt.kiosk.domain.order;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_item_extras")
public class OrderItemExtraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_extra_seq")
    @SequenceGenerator(
            name = "order_item_extra_seq",
            sequenceName = "order_item_extra_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItemEntity orderItem;

    @Column(name="extra_name", nullable = false)
    private String extraName;

    @Column(name="price_cents", nullable = false)
    private Integer priceCents;
}
