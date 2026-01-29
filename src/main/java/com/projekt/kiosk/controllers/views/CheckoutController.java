package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.dto.cart.Cart;
import com.projekt.kiosk.services.impl.CheckoutServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("cart")
public class CheckoutController {

    private final CheckoutServiceImpl checkoutService;

    public CheckoutController(CheckoutServiceImpl checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public String checkout(
            @ModelAttribute("cart") Cart cart,
            SessionStatus sessionStatus,
            RedirectAttributes redirectAttributes) {

        if (cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Cart is empty");
            return "redirect:/cart";
        }

        String orderNumber = checkoutService.checkout(cart).getOrderNumber();

        sessionStatus.setComplete();

        return "redirect:/order/summary/" + orderNumber;
    }
}

