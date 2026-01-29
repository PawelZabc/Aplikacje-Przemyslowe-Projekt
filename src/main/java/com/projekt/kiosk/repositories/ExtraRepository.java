package com.projekt.kiosk.repositories;

import com.projekt.kiosk.entities.ExtraEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtraRepository extends CrudRepository<ExtraEntity, Integer>,
        PagingAndSortingRepository<ExtraEntity, Integer> {
}
