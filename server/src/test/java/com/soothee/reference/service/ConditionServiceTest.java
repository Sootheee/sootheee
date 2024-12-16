package com.soothee.reference.service;

import com.soothee.config.TestConfig;
import com.soothee.reference.domain.Condition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
@Import(TestConfig.class)
class ConditionServiceTest {
    @Autowired
    private ConditionService conditionService;

    @Test
    void getConditionById() {
        //given
        //when
        Condition condition = conditionService.getConditionById(1L);
        //then
        Assertions.assertThat(condition.getCondValue()).isEqualTo(15);
    }
}