package com.projekt.kiosk.services;

import com.projekt.kiosk.dao.OrderDao;
import com.projekt.kiosk.dto.cart.Cart;
import com.projekt.kiosk.dto.cart.CartItemDto;
import com.projekt.kiosk.entities.order.OrderEntity;
import com.projekt.kiosk.enums.OrderType;
import com.projekt.kiosk.services.impl.CheckoutServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTests {

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Test
    void checkout_throwsException_whenCartIsEmpty() {
        // given
        Cart cart = new Cart();
        // clear items just to be sure, though default constructor might leave it empty
        cart.clear();

        // when/then
        assertThatThrownBy(() -> checkoutService.checkout(cart))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cart is empty");

        verifyNoInteractions(orderDao);
    }

    @Test
    void checkout_savesOrder_whenCartIsNotEmpty() {
        // given
        Cart cart = new Cart();
        CartItemDto item = new CartItemDto();
        item.setProductId(1);
        cart.addItem(item);
        cart.setOrderType(OrderType.DINE_IN);

        OrderEntity expectedOrder = new OrderEntity();
        expectedOrder.setId(100L);
        expectedOrder.setOrderNumber("ORD-123");
        expectedOrder.setTotalPriceCents(500);

        when(orderDao.saveOrderSnapshot(cart)).thenReturn(expectedOrder);

        // when
        OrderEntity result = checkoutService.checkout(cart);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getOrderNumber()).isEqualTo("ORD-123");

        verify(orderDao).saveOrderSnapshot(cart);
    }
}
