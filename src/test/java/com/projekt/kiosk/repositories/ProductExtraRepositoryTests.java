package com.projekt.kiosk.repositories;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.ExtraEntity;
import com.projekt.kiosk.entities.ProductEntity;
import com.projekt.kiosk.entities.ProductExtraEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductExtraRepositoryTests {

    private final ProductExtraRepository repository;
    private final ProductRepository productRepository;
    private final ExtraRepository extraRepository;

    @Autowired
    public ProductExtraRepositoryTests(
            ProductExtraRepository repository,
            ProductRepository productRepository,
            ExtraRepository extraRepository
    ) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.extraRepository = extraRepository;
    }

    @Test
    public void testSaveProductExtra() {
        ProductEntity product = productRepository.save(TestDataUtil.createTestProductA());
        ExtraEntity extra = extraRepository.save(TestDataUtil.createTestExtraA());

        ProductExtraEntity pe = ProductExtraEntity.builder()
                .product(product)
                .extra(extra)
                .build();

        repository.save(pe);

        List<ProductExtraEntity> result = repository.findByProductId(product.getId());
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getExtra().getId())
                .isEqualTo(extra.getId());
    }
}
