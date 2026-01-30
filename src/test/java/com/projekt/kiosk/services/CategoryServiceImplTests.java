package com.projekt.kiosk.services;

import com.projekt.kiosk.entities.CategoryEntity;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.repositories.CategoryRepository;
import com.projekt.kiosk.services.impl.CategoryServiceImpl;
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
class CategoryServiceImplTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void partialUpdate_updatesName_whenPresent() {
        // given
        Integer id = 1;

        CategoryEntity existing = new CategoryEntity();
        existing.setId(id);
        existing.setName("Old Name");

        CategoryEntity update = new CategoryEntity();
        update.setName("New Name");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(CategoryEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        CategoryEntity result = categoryService.partialUpdate(id, update);

        // then
        assertThat(result.getName()).isEqualTo("New Name");
        verify(categoryRepository).save(existing);
    }

    @Test
    void partialUpdate_ignoresNulls() {
        // given
        Integer id = 1;

        CategoryEntity existing = new CategoryEntity();
        existing.setId(id);
        existing.setName("Old Name");

        CategoryEntity update = new CategoryEntity();
        update.setName(null); // Null name should be ignored

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(CategoryEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        CategoryEntity result = categoryService.partialUpdate(id, update);

        // then
        assertThat(result.getName()).isEqualTo("Old Name");
        verify(categoryRepository).save(existing);
    }

    @Test
    void partialUpdate_throwsException_whenNotFound() {
        // given
        Integer id = 99;
        CategoryEntity update = new CategoryEntity();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> categoryService.partialUpdate(id, update))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category id=" + id + " not found");

        verify(categoryRepository, never()).save(any());
    }
}
