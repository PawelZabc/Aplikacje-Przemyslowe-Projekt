package com.projekt.kiosk.services;

import com.projekt.kiosk.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductEntity save(ProductEntity product);

    List<ProductEntity> readAll();

    Page<ProductEntity> readAll(Pageable pageable);

    Optional<ProductEntity> readOne(Integer id);

    boolean exists(Integer id);

    ProductEntity partialUpdate(Integer id, ProductEntity product);

    void delete(Integer id);
}

