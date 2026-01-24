package com.projekt.kiosk.services.inpl;

import com.projekt.kiosk.domain.ProductEntity;
import com.projekt.kiosk.repositories.ProductRepository;
import com.projekt.kiosk.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity save(ProductEntity product) {
        return productRepository.save(product);
    }

    @Override
    public List<ProductEntity> readAll() {
        return StreamSupport.stream(
                productRepository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @Override
    public Page<ProductEntity> readAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<ProductEntity> readOne(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public boolean exists(Integer id) {
        return productRepository.existsById(id);
    }

    @Override
    public ProductEntity partialUpdate(Integer id, ProductEntity product) {
        product.setId(id);

        return productRepository.findById(id).map(existing -> {
            Optional.ofNullable(product.getName()).ifPresent(existing::setName);
            Optional.ofNullable(product.getPriceCents()).ifPresent(existing::setPriceCents);
            return productRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public void delete(Integer id) {
        productRepository.deleteById(id);
    }
}
