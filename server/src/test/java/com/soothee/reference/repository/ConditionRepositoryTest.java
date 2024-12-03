package com.soothee.reference.repository;

import com.soothee.reference.domain.Condition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ConditionRepositoryTest {
    @Autowired
    private ConditionRepository conditionRepository;

    @Test
    void findByCondId() {
        Condition condition = conditionRepository.findByCondId(1L).orElseThrow();
        Assertions.assertThat(condition).isNotNull();
    }
}