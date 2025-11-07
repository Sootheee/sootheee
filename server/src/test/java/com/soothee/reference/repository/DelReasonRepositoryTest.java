package com.soothee.reference.repository;

import com.soothee.config.TestConfig;
import com.soothee.reference.domain.DelReason;
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
class DelReasonRepositoryTest {
    @Autowired
    private DelReasonRepository delReasonRepository;

    @Test
    void findByReasonId() {
        //given
        //when
        DelReason delReason = delReasonRepository.findByReasonId(CommonTestCode.DEL_REASON_ID1).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(delReason.getContent()).isNotNull();
    }
}