package com.projekt.kiosk.services.inpl;

import com.projekt.kiosk.dao.OrderDao;
import com.projekt.kiosk.domain.order.OrderEntity;
import com.projekt.kiosk.dtos.cart.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckoutService {

    private final OrderDao orderDao;

    public CheckoutService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Transactional
    public OrderEntity checkout(Cart cart) {
        if (cart.getItems().isEmpty()) throw new IllegalStateException("Cart is empty");
        return orderDao.saveOrderSnapshot(cart);
    }
}

