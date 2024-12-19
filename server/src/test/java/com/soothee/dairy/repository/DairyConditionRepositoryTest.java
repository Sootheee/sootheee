package com.soothee.dairy.repository;

import com.soothee.common.requestParam.MonthParam;
import com.soothee.config.TestConfig;
import com.soothee.dairy.domain.Dairy;
import com.soothee.stats.dto.ConditionRatio;
import com.soothee.stats.dto.MonthlyConditionsDTO;
import com.soothee.util.CommonTestCode;
import com.soothee.dairy.domain.DairyCondition;
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
        //given
        Dairy newDairy = commonTestCode.saveNewDairy();
        //when
        boolean result = dairyConditionRepository.existsByDairyDairyId(newDairy.getDairyId());
        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    void existsByDairyDairyIdY() {
        //given
        Dairy newDairy = commonTestCode.saveNewDairyCondition();
        //when
        boolean result = dairyConditionRepository.existsByDairyDairyId(newDairy.getDairyId());
        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void findMostOneCondIdInMonth() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        Double cnt = dairyConditionRepository.getAllDairyConditionCntInMonth(CommonTestCode.MEMBER_ID, monthParam).orElseThrow().getCount().doubleValue();
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
        MonthlyConditionsDTO result = dairyConditionRepository.getAllDairyConditionCntInMonth(CommonTestCode.MEMBER_ID, monthParam).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result.getCount()).isEqualTo(20);
    }
}
