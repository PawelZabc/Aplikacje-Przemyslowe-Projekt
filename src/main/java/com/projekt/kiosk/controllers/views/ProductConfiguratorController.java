package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.services.impl.ProductDetailsServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductConfiguratorController {

    private final ProductDetailsServiceImpl productDetailsService;

    public ProductConfiguratorController(ProductDetailsServiceImpl productDetailsService) {
        this.productDetailsService = productDetailsService;
    }

    @GetMapping("/product/{id}")
    public String configure(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productDetailsService.getProductDetails(id));
        return "product-configurator";
    }
}

