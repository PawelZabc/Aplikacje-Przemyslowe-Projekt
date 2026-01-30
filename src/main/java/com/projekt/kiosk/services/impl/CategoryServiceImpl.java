package com.projekt.kiosk.services.impl;

import com.projekt.kiosk.entities.CategoryEntity;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CategoryServiceImpl{

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryEntity save(CategoryEntity category) {
        log.info("Saving category: {}", category.getName());
        CategoryEntity saved = categoryRepository.save(category);
        log.debug("Category saved with id: {}", saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<CategoryEntity> readAll() {
        log.debug("Fetching all categories");
        List<CategoryEntity> categories = StreamSupport.stream(
                categoryRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
        log.info("Found {} categories", categories.size());
        return categories;
    }

    @Transactional(readOnly = true)
    public Page<CategoryEntity> readAll(Pageable pageable) {
        log.debug("Fetching categories with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return categoryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<CategoryEntity> readOne(Integer id) {
        log.debug("Fetching category by id: {}", id);
        return categoryRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean exists(Integer id) {
        boolean exists = categoryRepository.existsById(id);
        log.debug("Category exists check: id={}, exists={}", id, exists);
        return exists;
    }

    @Transactional
    public CategoryEntity partialUpdate(Integer id, CategoryEntity category) {
        log.info("Partial update for category id: {}", id);
        category.setId(id);

        return categoryRepository.findById(id)
                .map(existing -> {
                    Optional.ofNullable(category.getName()).ifPresent(name -> {
                        log.debug("Updating category name: {} -> {}", existing.getName(), name);
                        existing.setName(name);
                    });
                    return categoryRepository.save(existing);
                })
                .orElseThrow(() -> {
                    log.error("Category not found for partial update: id={}", id);
                    return new ResourceNotFoundException("Category id=" + id + " not found");
                });
    }

    @Transactional
    public void delete(Integer id) {
        log.info("Deleting category: id={}", id);
        categoryRepository.deleteById(id);
        log.debug("Category deleted: id={}", id);
    }
}

