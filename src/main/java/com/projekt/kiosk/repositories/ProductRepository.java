package com.projekt.kiosk.repositories;

import com.projekt.kiosk.entities.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Integer>,
        PagingAndSortingRepository<ProductEntity, Integer> {
}

