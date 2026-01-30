package com.projekt.kiosk.services.impl;

import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.exceptions.ResourceNotFoundException;
import com.projekt.kiosk.repositories.ExtraRepository;
import com.projekt.kiosk.services.ExtraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ExtraServiceImpl implements ExtraService {

    private final ExtraRepository extraRepository;

    public ExtraServiceImpl(ExtraRepository extraRepository) {
        this.extraRepository = extraRepository;
    }

    @Override
    @Transactional
    public ExtraEntity save(ExtraEntity extra) {
        log.info("Saving extra: {}", extra.getName());
        ExtraEntity saved = extraRepository.save(extra);
        log.debug("Extra saved with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExtraEntity> readAll() {
        log.debug("Fetching all extras");
        List<ExtraEntity> extras = StreamSupport.stream(
                extraRepository.findAll().spliterator(), false).collect(Collectors.toList());
        log.info("Found {} extras", extras.size());
        return extras;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExtraEntity> readAll(Pageable pageable) {
        log.debug("Fetching extras with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());
        return extraRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExtraEntity> readOne(Integer id) {
        log.debug("Fetching extra by id: {}", id);
        Optional<ExtraEntity> extra = extraRepository.findById(id);
        if (extra.isEmpty()) {
            log.debug("Extra not found: id={}", id);
        }
        return extra;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Integer id) {
        boolean exists = extraRepository.existsById(id);
        log.debug("Extra exists check: id={}, exists={}", id, exists);
        return exists;
    }

    @Override
    @Transactional
    public ExtraEntity partialUpdate(Integer id, ExtraEntity extra) {
        log.info("Partial update for extra id: {}", id);
        extra.setId(id);

        return extraRepository.findById(id).map(existing -> {
            Optional.ofNullable(extra.getName()).ifPresent(name -> {
                log.debug("Updating extra name: {} -> {}", existing.getName(), name);
                existing.setName(name);
            });
            Optional.ofNullable(extra.getPriceCents()).ifPresent(price -> {
                log.debug("Updating extra price: {} -> {}", existing.getPriceCents(), price);
                existing.setPriceCents(price);
            });
            return extraRepository.save(existing);
        }).orElseThrow(() -> {
            log.error("Extra not found for partial update: id={}", id);
            return new ResourceNotFoundException("Extra id=" + id + " not found");
        });
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting extra: id={}", id);
        extraRepository.deleteById(id);
        log.debug("Extra deleted: id={}", id);
    }
}
