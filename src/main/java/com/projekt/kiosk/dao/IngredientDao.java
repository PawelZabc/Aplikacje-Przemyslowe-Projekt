package com.projekt.kiosk.dao;

import com.projekt.kiosk.domain.Ingredient;
import org.springframework.jdbc.core.JdbcTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IngredientDao {
    void create(Ingredient ingredient);
    Optional<Ingredient> findById(int id);

    List<Ingredient> find();

    void update(Ingredient ingredient);

    void delete(int id);
}
