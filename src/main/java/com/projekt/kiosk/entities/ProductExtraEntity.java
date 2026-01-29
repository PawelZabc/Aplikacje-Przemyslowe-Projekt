package com.projekt.kiosk.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_extras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductExtraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(
//            name = "product_extra_seq",
//            sequenceName = "product_extra_seq",
//            allocationSize = 1
//    )
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "extra_id")
    private ExtraEntity extra;
}

