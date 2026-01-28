package com.projekt.kiosk.controllers.views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("orderType")
public class OrderTypeController {

    @GetMapping("/order-type")
    public String chooseOrderType() {
        return "order-type";
    }

    @PostMapping("/order-type")
    public String setOrderType(@RequestParam String type, Model model) {
        model.addAttribute("orderType", type);
        return "redirect:/menu";
    }
}

