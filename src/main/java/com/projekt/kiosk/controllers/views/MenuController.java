package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MenuController {

    private final ProductService productService;

    public MenuController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/menu")
    public String menu(Model model) {

        Map<String, List<ProductEntity>> productsByCategory =
                productService.readAll().stream()
                        .filter(p -> p.getCategory() != null)
                        .collect(Collectors.groupingBy(
                                p -> p.getCategory().getName(),
                                LinkedHashMap::new, // zachowuje kolejność
                                Collectors.toList()
                        ));

        model.addAttribute("productsByCategory", productsByCategory);
        return "menu";
    }
}

