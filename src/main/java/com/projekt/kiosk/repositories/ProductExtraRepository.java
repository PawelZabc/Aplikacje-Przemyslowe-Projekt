package com.projekt.kiosk.repositories;

import com.projekt.kiosk.entities.ProductExtraEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductExtraRepository
        extends CrudRepository<ProductExtraEntity, Integer> {

    List<ProductExtraEntity> findByProductId(Integer productId);

    boolean existsByProductIdAndExtraId(Integer productId, Integer extraId);

    void deleteByProductIdAndExtraId(Integer productId, Integer extraId);
}
