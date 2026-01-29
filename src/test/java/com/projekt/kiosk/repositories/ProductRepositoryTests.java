package com.projekt.kiosk.repositories;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveProduct() {
        ProductEntity product = TestDataUtil.createTestProductA();
        productRepository.save(product);

        Optional<ProductEntity> result = productRepository.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(product);
    }

    @Test
    public void testFindAll() {
        productRepository.save(TestDataUtil.createTestProductA());
        productRepository.save(TestDataUtil.createTestProductB());
        productRepository.save(TestDataUtil.createTestProductC());

        Iterable<ProductEntity> result = productRepository.findAll();

        assertThat(result).hasSize(3);
    }

    @Test
    public void testUpdateProduct() {
        ProductEntity product = TestDataUtil.createTestProductA();
        productRepository.save(product);

        product.setName("Updated");
        product.setPriceCents(999);

        productRepository.save(product);

        Optional<ProductEntity> result = productRepository.findById(1);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(product);
    }

    @Test
    public void testDeleteProduct() {
        ProductEntity product = TestDataUtil.createTestProductA();
        productRepository.save(product);

        productRepository.deleteById(product.getId());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }
}
