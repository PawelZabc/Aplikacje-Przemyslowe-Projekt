package com.projekt.kiosk.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString @EqualsAndHashCode
@Entity
@Table(name = "extras")
public class ExtraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(
//            name = "extra_seq",
//            sequenceName = "extra_seq",
//            allocationSize = 1
//    )
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "price_cents", nullable = false)
    private Integer priceCents;
}
