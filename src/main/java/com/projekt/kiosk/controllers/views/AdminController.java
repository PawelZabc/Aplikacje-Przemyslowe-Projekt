package com.projekt.kiosk.controllers.views;

import com.projekt.kiosk.dto.CategoryDto;
import com.projekt.kiosk.entities.CategoryEntity;
import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.dto.ExtraDto;
import com.projekt.kiosk.dto.IngredientDto;
import com.projekt.kiosk.dto.ProductDto;
import com.projekt.kiosk.mappers.CategoryMapper;
import com.projekt.kiosk.mappers.ExtraMapper;
import com.projekt.kiosk.mappers.IngredientMapper;
import com.projekt.kiosk.mappers.Mapper;
import com.projekt.kiosk.services.ExtraService;
import com.projekt.kiosk.services.IngredientService;
import com.projekt.kiosk.services.ProductService;
import com.projekt.kiosk.services.impl.CategoryServiceImpl;
import com.projekt.kiosk.services.impl.ProductExtraServiceImpl;
import com.projekt.kiosk.services.impl.ProductIngredientServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final ProductService productService;
    private final IngredientService ingredientService;
    private final ExtraService extraService;

    private final Mapper<ProductEntity, ProductDto> productMapper;
    private final ProductIngredientServiceImpl productIngredientService;
    private final ProductExtraServiceImpl productExtraService;
    private final Mapper<ExtraEntity, ExtraDto> extraMapper;
    private final Mapper<IngredientEntity, IngredientDto> ingredientMapper;

    private final CategoryServiceImpl categoryService;
    private final Mapper<CategoryEntity,CategoryDto> categoryMapper;

    public AdminController(ProductService productService,
                           IngredientService ingredientService,
                           ExtraService extraService,
                           Mapper<ProductEntity, ProductDto> productMapper,
                           ProductIngredientServiceImpl productIngredientService,
                           ProductExtraServiceImpl productExtraService,
                           ExtraMapper extraMapper,
                           IngredientMapper ingredientMapper,
                           CategoryServiceImpl categoryService,
                           Mapper<CategoryEntity,CategoryDto> categoryMapper) {
        this.productService = productService;
        this.ingredientService = ingredientService;
        this.extraService = extraService;
        this.productMapper = productMapper;
        this.productIngredientService = productIngredientService;
        this.productExtraService = productExtraService;
        this.extraMapper = extraMapper;
        this.ingredientMapper = ingredientMapper;
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }


    /* =======================
       DASHBOARD
       ======================= */

    @GetMapping
    public String adminHome(Model model) {
        List<ProductDto> products = productService.readAll()
                .stream()
                .map(productMapper::mapTo)
                .toList();

        model.addAttribute("products", products);
        List<ExtraDto> extras = extraService.readAll()
                .stream()
                .map(extraMapper::mapTo)
                .toList();

        model.addAttribute("extras", extras);
        List<IngredientDto> ingredients = ingredientService.readAll()
                .stream()
                .map(ingredientMapper::mapTo)
                .toList();
        List<CategoryDto> categories = categoryService.readAll()
                .stream()
                .map(categoryMapper::mapTo)
                .toList();
        model.addAttribute("categories", categories);


        model.addAttribute("ingredients", ingredients);
        return "admin";
    }

    /* =======================
       PRODUCTS
       ======================= */

    @GetMapping("/products")
    public String createProductPage() {
        return "admin/create-product";
    }

    @PostMapping("/products")
    public String createProduct(@RequestParam String name,
                                @RequestParam Integer priceCents,
                                @RequestParam Integer categoryId) {

        CategoryEntity category = categoryService.readOne(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        ProductEntity product = new ProductEntity();
        product.setName(name);
        product.setPriceCents(priceCents);
        product.setCategory(category);

        productService.save(product);
        log.info("Admin created product: {}", name);

        return "redirect:/admin/admin";
    }

    /* =======================
       INGREDIENTS
       ======================= */

    @GetMapping("/ingredients")
    public String createIngredientPage() {
        return "admin/create-ingredient";
    }

    @PostMapping("/ingredients")
    public String createIngredient(@RequestParam String name) {

        IngredientEntity ingredient = new IngredientEntity();
        ingredient.setName(name);

        ingredientService.save(ingredient);
        log.info("Admin created ingredient: {}", name);

        return "redirect:/admin/admin";
    }

    /* =======================
       EXTRAS
       ======================= */

    @GetMapping("/extras")
    public String createExtraPage() {
        return "admin/create-extra";
    }

    @PostMapping("/extras")
    public String createExtra(@RequestParam String name,
                              @RequestParam Integer priceCents) {

        ExtraEntity extra = new ExtraEntity();
        extra.setName(name);
        extra.setPriceCents(priceCents);

        extraService.save(extra);
        log.info("Admin created extra: {}", name);

        return "redirect:/admin/admin";
    }

    /* =======================
       LINK INGREDIENT → PRODUCT
       ======================= */

    @GetMapping("/products/{productId}/ingredient")
    public String addIngredientToProductPage(@PathVariable Integer productId,
                                             Model model) {
        model.addAttribute("productId", productId);
        return "admin/add-ingredient-to-product";
    }

    @PostMapping("/products/{productId}/ingredient")
    public String addIngredientToProduct(@PathVariable Integer productId,
                                         @RequestParam Integer ingredientId) {

        productIngredientService.create(productId, ingredientId);
        log.info("Linked ingredient {} to product {}", ingredientId, productId);

        return "redirect:/admin/admin";
    }

    /* =======================
       LINK EXTRA → PRODUCT
       ======================= */

    @GetMapping("/products/{productId}/extra")
    public String addExtraToProductPage(@PathVariable Integer productId,
                                        Model model) {
        model.addAttribute("productId", productId);
        return "admin/add-extra-to-product";
    }

    @PostMapping("/products/{productId}/extra")
    public String addExtraToProduct(@PathVariable Integer productId,
                                    @RequestParam Integer extraId) {

        productExtraService.create(productId, extraId);
        log.info("Linked extra {} to product {}", extraId, productId);

        return "redirect:/admin/admin";
    }

    @GetMapping("/categories")
    public String createCategoryPage() {
        return "admin/create-category"; // the Thymeleaf template for creating a category
    }

    @PostMapping("/categories")
    public String createCategory(@RequestParam String name) {

        CategoryEntity category = new CategoryEntity();
        category.setName(name);

        categoryService.save(category); // make sure you inject CategoryService
        log.info("Admin created category: {}", name);

        return "redirect:/admin/admin";
    }
}
