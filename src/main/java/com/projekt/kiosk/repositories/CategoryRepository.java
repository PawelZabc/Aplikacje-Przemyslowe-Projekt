package com.projekt.kiosk.repositories;

import com.projekt.kiosk.entities.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer>,
        PagingAndSortingRepository<CategoryEntity, Integer> {
}
