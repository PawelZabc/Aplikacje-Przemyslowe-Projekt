package com.projekt.kiosk.dao;

import com.projekt.kiosk.domain.order.OrderEntity;
import com.projekt.kiosk.dtos.cart.Cart;

public interface OrderDao {
    OrderEntity saveOrderSnapshot(Cart cart);
}

