package com.projekt.kiosk.services;

import com.projekt.kiosk.domain.Ingredient;
import com.projekt.kiosk.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceInpl implements IngredientService {

    private IngredientRepository ingredientRepository;

    public IngredientServiceInpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient createIngredient(Ingredient ingredient) {
        return this.ingredientRepository.save(ingredient);
    }

    @Override
    public Iterable<Ingredient> readAll() {
        return this.ingredientRepository.findAll();
    }
}
