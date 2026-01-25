package com.projekt.kiosk.services.inpl;

import com.projekt.kiosk.domain.ExtraEntity;
import com.projekt.kiosk.domain.ProductEntity;
import com.projekt.kiosk.domain.ProductExtraEntity;
import com.projekt.kiosk.repositories.ExtraRepository;
import com.projekt.kiosk.repositories.ProductExtraRepository;
import com.projekt.kiosk.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductExtraService {

    private final ProductExtraRepository repository;
    private final ProductRepository productRepository;
    private final ExtraRepository extraRepository;

    public ProductExtraService(
            ProductExtraRepository repository,
            ProductRepository productRepository,
            ExtraRepository extraRepository
    ) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.extraRepository = extraRepository;
    }

    public List<ProductExtraEntity> readByProduct(Integer productId) {
        return repository.findByProductId(productId);
    }

    public ProductExtraEntity create(Integer productId, Integer extraId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ExtraEntity extra = extraRepository.findById(extraId)
                .orElseThrow(() -> new RuntimeException("Extra not found"));

        if (repository.existsByProductIdAndExtraId(productId, extraId)) {
            throw new RuntimeException("Extra already assigned to product");
        }

        ProductExtraEntity pe = ProductExtraEntity.builder()
                .product(product)
                .extra(extra)
                .build();

        return repository.save(pe);
    }

    public void delete(Integer productId, Integer extraId) {
        repository.deleteByProductIdAndExtraId(productId, extraId);
    }
}

