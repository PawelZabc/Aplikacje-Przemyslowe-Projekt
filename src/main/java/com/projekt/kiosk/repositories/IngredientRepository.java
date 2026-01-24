package com.projekt.kiosk.repositories;

import com.projekt.kiosk.domain.IngredientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<IngredientEntity, Integer>,
        PagingAndSortingRepository<IngredientEntity, Integer> {
}
