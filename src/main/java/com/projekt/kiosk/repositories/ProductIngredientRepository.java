package com.projekt.kiosk.repositories;

import com.projekt.kiosk.domain.ProductIngredientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductIngredientRepository
        extends CrudRepository<ProductIngredientEntity, Integer> {

    List<ProductIngredientEntity> findByProductId(Integer productId);

    boolean existsByProductIdAndIngredientId(Integer productId, Integer ingredientId);

    void deleteByProductIdAndIngredientId(Integer productId, Integer ingredientId);
}
