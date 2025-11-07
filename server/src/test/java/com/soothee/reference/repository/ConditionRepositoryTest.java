package com.soothee.reference.repository;

import com.soothee.config.TestConfig;
import com.soothee.reference.domain.Condition;
import com.soothee.util.CommonTestCode;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Slf4j
@Import(TestConfig.class)
class ConditionRepositoryTest {
    @Autowired
    private ConditionRepository conditionRepository;

    @Test
    void findByCondId() {
        //given
        //when
        Condition condition = conditionRepository.findByCondId(CommonTestCode.COND_ID1).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(condition.getCondValue()).isEqualTo(15);
    }
}