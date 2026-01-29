package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.entities.order.OrderEntity;
import com.projekt.kiosk.repositories.order.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/order")
public class OrderSummaryController {

    private final OrderRepository orderRepository;

    public OrderSummaryController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/summary/{orderNumber}")
    public String summary(
            @PathVariable String orderNumber,
            Model model) {

        OrderEntity order = orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Order not found"
                        )
                );

        model.addAttribute("order", order);
        return "order-summary";
    }
}
