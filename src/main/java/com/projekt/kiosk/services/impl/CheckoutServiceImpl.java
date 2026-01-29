package com.projekt.kiosk.services.impl;

import com.projekt.kiosk.dao.OrderDao;
import com.projekt.kiosk.entities.order.OrderEntity;
import com.projekt.kiosk.dto.cart.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckoutServiceImpl {

    private final OrderDao orderDao;

    public CheckoutServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Transactional
    public OrderEntity checkout(Cart cart) {
        if (cart.getItems().isEmpty()) throw new IllegalStateException("Cart is empty");
        return orderDao.saveOrderSnapshot(cart);
    }
}

