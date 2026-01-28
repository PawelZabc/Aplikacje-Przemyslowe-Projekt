package com.projekt.kiosk.repositories.order;

import com.projekt.kiosk.domain.order.OrderItemExtraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemExtraRepository extends JpaRepository<OrderItemExtraEntity, Long> {}
