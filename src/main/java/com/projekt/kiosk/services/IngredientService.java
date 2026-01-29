package com.projekt.kiosk.services;

import com.projekt.kiosk.entities.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface IngredientService {
    IngredientEntity save(IngredientEntity ingredient);

    List<IngredientEntity> readAll();

    Page<IngredientEntity> readAll(Pageable pageable);

    Optional<IngredientEntity> readOne(Integer id);

    boolean exists(Integer id);

    IngredientEntity partialUpdate(Integer id, IngredientEntity ingredient);

    void delete(Integer id);
}
