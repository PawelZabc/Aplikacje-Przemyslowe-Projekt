package com.projekt.kiosk.repositories.order;

import com.projekt.kiosk.domain.order.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {}



