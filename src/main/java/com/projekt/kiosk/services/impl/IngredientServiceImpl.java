package com.projekt.kiosk.services.impl;

import com.projekt.kiosk.entities.IngredientEntity;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.repositories.IngredientRepository;
import com.projekt.kiosk.services.IngredientService;
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
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional
    public IngredientEntity save(IngredientEntity ingredient) {
        log.info("Saving ingredient: {}", ingredient.getName());
        IngredientEntity saved = ingredientRepository.save(ingredient);
        log.debug("Ingredient saved with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientEntity> readAll() {
        log.debug("Fetching all ingredients");
        List<IngredientEntity> ingredients = StreamSupport.stream(
                ingredientRepository.findAll().spliterator(), false).collect(Collectors.toList());
        log.info("Found {} ingredients", ingredients.size());
        return ingredients;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IngredientEntity> readAll(Pageable pageable) {
        log.debug("Fetching ingredients with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return ingredientRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IngredientEntity> readOne(Integer id) {
        log.debug("Fetching ingredient by id: {}", id);
        Optional<IngredientEntity> ingredient = ingredientRepository.findById(id);
        if (ingredient.isEmpty()) {
            log.debug("Ingredient not found: id={}", id);
        }
        return ingredient;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Integer id) {
        boolean exists = ingredientRepository.existsById(id);
        log.debug("Ingredient exists check: id={}, exists={}", id, exists);
        return exists;
    }

    @Override
    @Transactional
    public IngredientEntity partialUpdate(Integer id, IngredientEntity ingredient) {
        log.info("Partial update for ingredient id: {}", id);
        ingredient.setId(id);

        return ingredientRepository.findById(id).map(existing -> {
            Optional.ofNullable(ingredient.getName()).ifPresent(name -> {
                log.debug("Updating ingredient name: {} -> {}", existing.getName(), name);
                existing.setName(name);
            });
            return ingredientRepository.save(existing);
        }).orElseThrow(() -> {
            log.error("Ingredient not found for partial update: id={}", id);
            return new ResourceNotFoundException("Ingredient id=" + id + " not found");
        });
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting ingredient: id={}", id);
        ingredientRepository.deleteById(id);
        log.debug("Ingredient deleted: id={}", id);
    }
}
