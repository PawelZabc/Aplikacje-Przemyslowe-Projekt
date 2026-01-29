package com.projekt.kiosk.services;

import com.projekt.kiosk.entities.*;
import com.projekt.kiosk.dto.ExtraDto;
import com.projekt.kiosk.dto.IngredientDto;
import com.projekt.kiosk.dto.ProductDetailsDto;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.repositories.ProductExtraRepository;
import com.projekt.kiosk.repositories.ProductIngredientRepository;
import com.projekt.kiosk.repositories.ProductRepository;
import com.projekt.kiosk.services.impl.ProductDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductDetailsServiceTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductIngredientRepository productIngredientRepository;

    @Mock
    private ProductExtraRepository productExtraRepository;

    @InjectMocks
    private ProductDetailsServiceImpl productDetailsService;

    @Test
    void getProductDetails_returnsProductWithIngredientsAndExtras() {
        // given
        ProductEntity product = new ProductEntity();
        product.setId(1);
        product.setName("Burger");
        product.setPriceCents(1500);

        IngredientEntity ingredient = new IngredientEntity();
        ingredient.setId(10);
        ingredient.setName("Cheese");

        ProductIngredientEntity pi = new ProductIngredientEntity();
        pi.setIngredient(ingredient);

        ExtraEntity extra = new ExtraEntity();
        extra.setId(20);
        extra.setName("Bacon");
        extra.setPriceCents(300);

        ProductExtraEntity pe = new ProductExtraEntity();
        pe.setExtra(extra);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productIngredientRepository.findByProductId(1))
                .thenReturn(List.of(pi));
        when(productExtraRepository.findByProductId(1))
                .thenReturn(List.of(pe));

        // when
        ProductDetailsDto result = productDetailsService.getProductDetails(1);

        // then
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Burger");
        assertThat(result.getPriceCents()).isEqualTo(1500);

        assertThat(result.getIngredients())
                .hasSize(1)
                .first()
                .extracting(IngredientDto::getName)
                .isEqualTo("Cheese");

        assertThat(result.getExtras())
                .hasSize(1)
                .first()
                .extracting(ExtraDto::getName, ExtraDto::getPriceCents)
                .containsExactly("Bacon", 300);
    }

    @Test
    void getProductDetails_throwsException_whenProductNotFound() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productDetailsService.getProductDetails(99))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found");
    }
}

