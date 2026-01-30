package com.projekt.kiosk.services;

import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.repositories.ProductRepository;
import com.projekt.kiosk.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void partialUpdate_updatesFields_whenPresent() {
        // given
        Integer id = 1;

        ProductEntity existing = new ProductEntity();
        existing.setId(id);
        existing.setName("Old Burger");
        existing.setPriceCents(1000);

        ProductEntity update = new ProductEntity();
        update.setName("New Burger");
        update.setPriceCents(1200);

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        ProductEntity result = productService.partialUpdate(id, update);

        // then
        assertThat(result.getName()).isEqualTo("New Burger");
        assertThat(result.getPriceCents()).isEqualTo(1200);
        verify(productRepository).save(existing);
    }

    @Test
    void partialUpdate_ignoresNulls() {
        // given
        Integer id = 1;

        ProductEntity existing = new ProductEntity();
        existing.setId(id);
        existing.setName("Old Burger");
        existing.setPriceCents(1000);

        ProductEntity update = new ProductEntity();
        // All fields null

        when(productRepository.findById(id)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        ProductEntity result = productService.partialUpdate(id, update);

        // then
        assertThat(result.getName()).isEqualTo("Old Burger");
        assertThat(result.getPriceCents()).isEqualTo(1000);
        verify(productRepository).save(existing);
    }

    @Test
    void partialUpdate_throwsException_whenNotFound() {
        // given
        Integer id = 99;
        ProductEntity update = new ProductEntity();

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> productService.partialUpdate(id, update))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product id=" + id + " not found");

        verify(productRepository, never()).save(any());
    }
}
