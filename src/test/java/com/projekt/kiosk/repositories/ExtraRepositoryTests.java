package com.projekt.kiosk.repositories;

import com.projekt.kiosk.TestDataUtil;
import com.projekt.kiosk.entities.ExtraEntity;
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
public class ExtraRepositoryTests {

    @Autowired
    private ExtraRepository extraRepository;

    @Test
    public void testSaveExtra() {
        ExtraEntity extra = TestDataUtil.createTestExtraA();
        extraRepository.save(extra);

        Optional<ExtraEntity> result = extraRepository.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(extra);
    }

    @Test
    public void testFindAll() {
        ExtraEntity extraA = TestDataUtil.createTestExtraA();
        extraRepository.save(extraA);

        ExtraEntity extraB = TestDataUtil.createTestExtraB();
        extraRepository.save(extraB);

        ExtraEntity extraC = TestDataUtil.createTestExtraC();
        extraRepository.save(extraC);

        Iterable<ExtraEntity> result = extraRepository.findAll();

        assertThat(result)
                .hasSize(3)
                .containsExactly(extraA, extraB, extraC);
    }

    @Test
    public void testUpdateExtra() {
        ExtraEntity extra = TestDataUtil.createTestExtraA();
        extraRepository.save(extra);

        extra.setName("Updated Extra");
        extra.setPriceCents(250);

        extraRepository.save(extra);

        Optional<ExtraEntity> result = extraRepository.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(extra);
    }

    @Test
    public void testDeleteExtra() {
        ExtraEntity extra = TestDataUtil.createTestExtraA();
        extraRepository.save(extra);

        extraRepository.deleteById(extra.getId());

        Optional<ExtraEntity> result = extraRepository.findById(extra.getId());
        assertThat(result).isNotPresent();
    }
}
