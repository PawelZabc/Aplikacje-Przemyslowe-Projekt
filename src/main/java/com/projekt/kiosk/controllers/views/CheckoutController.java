package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.domain.order.OrderEntity;
import com.projekt.kiosk.dtos.cart.Cart;
import com.projekt.kiosk.services.inpl.CheckoutService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("cart")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
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

