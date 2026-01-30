package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.dto.cart.Cart;
import com.projekt.kiosk.enums.OrderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes({ "orderType", "cart" })
@Slf4j
public class OrderTypeController {

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @GetMapping("/order-type")
    public String chooseOrderType() {
        return "order-type";
    }

    @PostMapping("/order-type")
    public String setOrderType(
            @RequestParam String type,
            @ModelAttribute("cart") Cart cart) {

        OrderType orderType = OrderType.fromString(type);
        cart.setOrderType(orderType);

        log.info("Order type set to: {}", orderType);

        return "redirect:/menu";
    }
}
