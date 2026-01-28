package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.domain.ExtraEntity;
import com.projekt.kiosk.domain.IngredientEntity;
import com.projekt.kiosk.domain.ProductEntity;
import com.projekt.kiosk.dtos.ExtraDto;
import com.projekt.kiosk.dtos.IngredientDto;
import com.projekt.kiosk.dtos.ProductDetailsDto;
import com.projekt.kiosk.dtos.cart.Cart;
import com.projekt.kiosk.dtos.cart.CartItemDto;
import com.projekt.kiosk.dtos.cart.CartItemExtraDto;
import com.projekt.kiosk.dtos.cart.CartItemIngredientDto;
import com.projekt.kiosk.mappers.ExtraMapper;
import com.projekt.kiosk.mappers.IngredientMapper;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.repositories.ExtraRepository;
import com.projekt.kiosk.repositories.IngredientRepository;
import com.projekt.kiosk.repositories.ProductRepository;
import com.projekt.kiosk.services.ProductService;
import com.projekt.kiosk.services.inpl.ProductDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes("cart")
@RequestMapping("/cart")
public class CartController {

    ProductRepository productRepository;
    IngredientRepository ingredientRepository;
    ExtraRepository extraRepository;
    ProductDetailsService productDetailsService;
    private Mapper<IngredientEntity, IngredientDto> ingredientMapper;
    private Mapper<ExtraEntity, ExtraDto> extraMapper;


    public CartController(ProductRepository productRepository, IngredientRepository ingredientRepository, ExtraRepository extraRepository
            ,ProductDetailsService productService, Mapper<IngredientEntity, IngredientDto> ingredientMapper ,Mapper<ExtraEntity, ExtraDto> extraMapper ) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
        this.extraRepository = extraRepository;
        this.productDetailsService = productService;
        this.ingredientMapper = ingredientMapper;
        this.extraMapper = extraMapper;
    }
    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam Integer productId,
            @RequestParam(required = false) List<Integer> removedIngredientIds,
            @RequestParam(required = false) List<Integer> extraIds,
            @RequestParam int quantity,
            @ModelAttribute("cart") Cart cart) {

        ProductEntity product = productRepository.findById(productId).orElseThrow();

        CartItemDto item = new CartItemDto();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setBasePriceCents(product.getPriceCents());
        item.setQuantity(quantity);

        if (removedIngredientIds != null) {
            ingredientRepository.findAllById(removedIngredientIds)
                    .forEach(ing -> {
                IngredientDto dto = ingredientMapper.mapTo(ing);
                item.getRemovedIngredients().add(dto);
            });

        }

        if (extraIds != null) {
            extraRepository.findAllById(extraIds)
                    .forEach(extra -> {
                        ExtraDto dto = extraMapper.mapTo(extra);
                        item.getAddedExtras().add(dto);
                    });
        }

        cart.addItem(item);

        return "redirect:/cart";
    }
    @GetMapping("/remove/{idx}")
    public String removeItem(
            @PathVariable int idx,
            @ModelAttribute("cart") Cart cart) {

        cart.removeItem(idx);
        return "redirect:/cart";
    }


    @GetMapping
    public String cart(@ModelAttribute("cart") Cart cart) {
        return "cart";
    }


    @GetMapping("/edit/{idx}")
    public String editItem(
            @PathVariable int idx,
            @ModelAttribute("cart") Cart cart,
            Model model) {

        CartItemDto item = cart.getItem(idx);
        model.addAttribute("item", item);
        model.addAttribute("itemIndex", idx);

        ProductDetailsDto product =
                productDetailsService.getProductDetails(item.getProductId());

        model.addAttribute("product", product);
        return "cart-edit";
    }

    @PostMapping("/edit/{idx}")
    public String updateItem(
            @PathVariable int idx,
            @RequestParam int quantity,
            @RequestParam(required = false) List<Integer> removedIngredientIds,
            @RequestParam(required = false) List<Integer> extraIds,
            @ModelAttribute("cart") Cart cart) {

        CartItemDto item = cart.getItem(idx);

        item.setQuantity(quantity);
        item.getRemovedIngredients().clear();
        item.getAddedExtras().clear();

        if (removedIngredientIds != null) {
            ingredientRepository.findAllById(removedIngredientIds)
                    .forEach(ing -> {
                        IngredientDto dto = ingredientMapper.mapTo(ing);
                        item.getRemovedIngredients().add(dto);
                    });

        }

        if (extraIds != null) {
            extraRepository.findAllById(extraIds)
                    .forEach(ing -> {
                        ExtraDto dto = extraMapper.mapTo(ing);
                        item.getAddedExtras().add(dto);
                    });
        }

        return "redirect:/cart";
    }



}
