package com.projekt.kiosk.services.impl;

import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.dto.ExtraDto;
import com.projekt.kiosk.dto.IngredientDto;
import com.projekt.kiosk.dto.ProductDetailsDto;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.repositories.ProductExtraRepository;
import com.projekt.kiosk.repositories.ProductIngredientRepository;
import com.projekt.kiosk.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductDetailsServiceImpl {

    private final ProductRepository productRepository;
    private final ProductIngredientRepository productIngredientRepository;
    private final ProductExtraRepository productExtraRepository;

    public ProductDetailsServiceImpl(
            ProductRepository productRepository,
            ProductIngredientRepository productIngredientRepository,
            ProductExtraRepository productExtraRepository
    ) {
        this.productRepository = productRepository;
        this.productIngredientRepository = productIngredientRepository;
        this.productExtraRepository = productExtraRepository;
    }

    public ProductDetailsDto getProductDetails(Integer productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        List<IngredientDto> ingredients =
                productIngredientRepository.findByProductId(productId)
                        .stream()
                        .map(pi -> new IngredientDto(
                                pi.getIngredient().getId(),
                                pi.getIngredient().getName()
                        ))
                        .toList();

        List<ExtraDto> extras =
                productExtraRepository.findByProductId(productId)
                        .stream()
                        .map(pe -> new ExtraDto(
                                pe.getExtra().getId(),
                                pe.getExtra().getName(),
                                pe.getExtra().getPriceCents()
                        ))
                        .toList();

        return new ProductDetailsDto(
                product.getId(),
                product.getName(),
                product.getPriceCents(),
                ingredients,
                extras
        );
    }
}
