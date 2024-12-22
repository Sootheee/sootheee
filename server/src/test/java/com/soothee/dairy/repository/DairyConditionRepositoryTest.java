package com.soothee.dairy.repository;

import com.soothee.common.requestParam.MonthParam;
import com.soothee.config.TestConfig;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.dairy.domain.Dairy;
import com.soothee.stats.dto.ConditionRatio;
import com.soothee.stats.dto.MonthlyConditionsDTO;
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
@Slf4j
@ActiveProfiles("test")
@Import(TestConfig.class)
class DairyConditionRepositoryTest {
    @Autowired
    private DairyConditionRepository dairyConditionRepository;
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc() {
        //given
        //when
        DairyCondition dairyCondition = dairyConditionRepository.findByDairyDairyIdAndIsDeleteOrderByOrderNoAsc(CommonTestCode.DAIRY_ID1, "N")
                                                                    .orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(dairyCondition.getCondition().getCondId()).isEqualTo(1L);
    }

    @Test
    void existsByDairyDairyIdN() {
        try {
            //given
            Dairy newDairy = commonTestCode.saveNewDairy();
            //when
            boolean result = dairyConditionRepository.existsByDairyDairyId(newDairy.getDairyId());
            //then
            Assertions.assertThat(result).isFalse();
        } catch (IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void existsByDairyDairyIdY() {
        try {
            //given
            Dairy newDairy = commonTestCode.saveNewDairyCondition();
            //when
            boolean result = dairyConditionRepository.existsByDairyDairyId(newDairy.getDairyId());
            //then
            Assertions.assertThat(result).isTrue();
        } catch (IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void findMostOneCondIdInMonth() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        Integer cnt = dairyConditionRepository.getAllDairyConditionCntInMonth(CommonTestCode.MEMBER_ID, monthParam);
        //when
        ConditionRatio result = dairyConditionRepository.findConditionRatioListInMonth(CommonTestCode.MEMBER_ID, monthParam,1, cnt).orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(result.getCondId()).isEqualTo(1L);
    }

    @Test
    void getAllDairyConditionCntInMonth() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        Integer result = dairyConditionRepository.getAllDairyConditionCntInMonth(CommonTestCode.MEMBER_ID, monthParam);
        //then
        Assertions.assertThat(result).isEqualTo(20);
    }
}
