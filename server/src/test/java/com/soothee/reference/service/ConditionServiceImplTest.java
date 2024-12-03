package com.soothee.reference.service;

import com.soothee.reference.domain.Condition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
class ConditionServiceImplTest {
    @Autowired
    private ConditionService conditionService;

    @Test
    void getConditionById() {
        Condition condition = conditionService.getConditionById(1L);
        Assertions.assertThat(condition.getCondId()).isEqualTo(1L);
    }
}