package com.projekt.kiosk.services;

import com.projekt.kiosk.entities.ExtraEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ExtraService {

    ExtraEntity save(ExtraEntity extra);

    List<ExtraEntity> readAll();

    Page<ExtraEntity> readAll(Pageable pageable);

    Optional<ExtraEntity> readOne(Integer id);

    boolean exists(Integer id);

    ExtraEntity partialUpdate(Integer id, ExtraEntity extra);

    void delete(Integer id);
}
