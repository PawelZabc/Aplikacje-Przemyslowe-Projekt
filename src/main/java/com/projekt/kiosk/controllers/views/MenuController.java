package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    private final ProductService productService;

    public MenuController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("products", productService.readAll());
        return "menu";
    }
}
