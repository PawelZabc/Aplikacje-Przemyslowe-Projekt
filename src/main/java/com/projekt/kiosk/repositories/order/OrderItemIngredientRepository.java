package com.projekt.kiosk.repositories.order;

import com.projekt.kiosk.domain.order.OrderItemIngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemIngredientRepository extends JpaRepository<OrderItemIngredientEntity, Long> {}