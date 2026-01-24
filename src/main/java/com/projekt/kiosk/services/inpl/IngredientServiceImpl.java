package com.projekt.kiosk.services.inpl;

import com.projekt.kiosk.domain.IngredientEntity;
import com.projekt.kiosk.repositories.IngredientRepository;
import com.projekt.kiosk.services.IngredientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IngredientServiceImpl implements IngredientService {

    private IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientEntity save(IngredientEntity ingredient) {
        return this.ingredientRepository.save(ingredient);
    }

    @Override
    public List<IngredientEntity> readAll() {
        return StreamSupport.stream(
                this.ingredientRepository.findAll().spliterator()
                ,false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<IngredientEntity> readAll(Pageable pageable) {
        return ingredientRepository.findAll(pageable);
    }

    @Override
    public Optional<IngredientEntity> readOne(Integer id) {
        return ingredientRepository.findById(id);
    }

    @Override
    public boolean exists(Integer id) {
        return ingredientRepository.existsById(id);
    }

    @Override
    public IngredientEntity partialUpdate(Integer id, IngredientEntity ingredient) {
        ingredient.setId(id);

        return ingredientRepository.findById(id).map(existingIngredient ->{
           Optional.ofNullable(ingredient.getName()).ifPresent(existingIngredient::setName);
           return ingredientRepository.save(existingIngredient);
        }).orElseThrow(()->new RuntimeException("Ingredient not found"));
    }

    @Override
    public void delete(Integer id) {
        ingredientRepository.deleteById(id);
    }
}
