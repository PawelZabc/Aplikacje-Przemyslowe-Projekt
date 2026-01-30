package com.projekt.kiosk.services.impl;

import com.projekt.kiosk.dao.OrderDao;
import com.projekt.kiosk.entities.order.OrderEntity;
import com.projekt.kiosk.dto.cart.Cart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CheckoutServiceImpl {

    private final OrderDao orderDao;

    public CheckoutServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Transactional
    public OrderEntity checkout(Cart cart) {
        log.info("Starting checkout process. Items: {}, Order type: {}",
                cart.getItems().size(), cart.getOrderType());

        if (cart.getItems().isEmpty()) {
            log.error("Checkout failed: Cart is empty");
            throw new IllegalStateException("Cart is empty");
        }

        OrderEntity order = orderDao.saveOrderSnapshot(cart);

        log.info("Checkout completed. Order number: {}, Total: {} cents",
                order.getOrderNumber(), order.getTotalPriceCents());

        return order;
    }
}
