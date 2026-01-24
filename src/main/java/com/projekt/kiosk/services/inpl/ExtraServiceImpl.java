package com.projekt.kiosk.services.inpl;

import com.projekt.kiosk.domain.ExtraEntity;
import com.projekt.kiosk.repositories.ExtraRepository;
import com.projekt.kiosk.services.ExtraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExtraServiceImpl implements ExtraService {

    private final ExtraRepository extraRepository;

    public ExtraServiceImpl(ExtraRepository extraRepository) {
        this.extraRepository = extraRepository;
    }

    @Override
    public ExtraEntity save(ExtraEntity extra) {
        return extraRepository.save(extra);
    }

    @Override
    public List<ExtraEntity> readAll() {
        return StreamSupport.stream(
                extraRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @Override
    public Page<ExtraEntity> readAll(Pageable pageable) {
        return extraRepository.findAll(pageable);
    }

    @Override
    public Optional<ExtraEntity> readOne(Integer id) {
        return extraRepository.findById(id);
    }

    @Override
    public boolean exists(Integer id) {
        return extraRepository.existsById(id);
    }

    @Override
    public ExtraEntity partialUpdate(Integer id, ExtraEntity extra) {
        extra.setId(id);

        return extraRepository.findById(id).map(existingExtra -> {
            Optional.ofNullable(extra.getName()).ifPresent(existingExtra::setName);
            Optional.ofNullable(extra.getPriceCents()).ifPresent(existingExtra::setPriceCents);
            return extraRepository.save(existingExtra);
        }).orElseThrow(() -> new RuntimeException("Extra not found"));
    }

    @Override
    public void delete(Integer id) {
        extraRepository.deleteById(id);
    }
}
