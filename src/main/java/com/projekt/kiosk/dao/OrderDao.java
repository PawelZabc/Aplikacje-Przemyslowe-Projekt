package com.projekt.kiosk.dao;

import com.projekt.kiosk.entities.order.OrderEntity;
import com.projekt.kiosk.dto.cart.Cart;

public interface OrderDao {
    OrderEntity saveOrderSnapshot(Cart cart);
}

