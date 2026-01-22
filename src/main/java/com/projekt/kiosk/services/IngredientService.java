package com.projekt.kiosk.services;

import com.projekt.kiosk.domain.Ingredient;

public interface IngredientService {
    Ingredient createIngredient(Ingredient ingredient);

    Iterable<Ingredient> readAll();
}
