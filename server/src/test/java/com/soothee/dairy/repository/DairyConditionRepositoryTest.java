package com.soothee.dairy.repository;

import com.soothee.common.constants.BooleanYN;
import com.soothee.config.TestConfig;
import com.soothee.dairy.domain.Dairy;
import com.soothee.stats.controller.response.ConditionRatio;
import com.soothee.util.CommonTestCode;
import com.soothee.dairy.domain.DairyCondition;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableJpaAuditing
@ActiveProfiles("test")
@Slf4j
@Import(TestConfig.class)
class DairyConditionRepositoryTest {
    @Autowired
    private DairyConditionRepository dairyConditionRepository;
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void findByDairyDairyIdAndDairyIsDeleteAndIsDeleteOrderByOrderNoAsc() {
        //given
        //when
        DairyCondition dairyCondition = dairyConditionRepository.findDairyConditionListByDairyId(CommonTestCode.DAIRY_ID1).orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(dairyCondition.getCondition().getCondId()).isEqualTo(CommonTestCode.COND_ID1);
    }

    @Test
    void existsByDairyDairyIdAndDairyIsDeleteAndIsDeleteN() {
        //given
        Dairy newDairy = commonTestCode.saveNewDairy();
        //when
        boolean result = dairyConditionRepository.existsByDairyDairyIdAndDairyIsDeleteAndIsDelete(newDairy.getDairyId(), BooleanYN.N, BooleanYN.N);
        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    void existsByDairyDairyIdAndDairyIsDeleteAndIsDeleteY() {
        //given
        Dairy newDairy = commonTestCode.saveNewDairyCondition();
        //when
        boolean result = dairyConditionRepository.existsByDairyDairyIdAndDairyIsDeleteAndIsDelete(newDairy.getDairyId(), BooleanYN.N, BooleanYN.N);
        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void findMostOneCondIdInMonth() {
        //given
        Integer cnt = dairyConditionRepository.getSelectedConditionsCountInMonth(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.MONTH).orElseThrow(NullPointerException::new);
        //when
        ConditionRatio result = dairyConditionRepository.findConditionRatioInMonth(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.MONTH,1, cnt).orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(result.getCondId()).isEqualTo(CommonTestCode.COND_ID1);
    }

    @Test
    void getAllDairyConditionCntInMonth() {
        //given
        //when
        Integer result = dairyConditionRepository.getSelectedConditionsCountInMonth(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.MONTH).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result).isEqualTo(20);
    }
}
