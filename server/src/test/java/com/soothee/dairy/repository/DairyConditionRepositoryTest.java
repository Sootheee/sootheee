package com.soothee.dairy.repository;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.config.TestConfig;
import com.soothee.custom.exception.IncorrectValueException;
import com.soothee.custom.exception.NullValueException;
import com.soothee.dairy.domain.Dairy;
import com.soothee.stats.dto.ConditionRatio;
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
        DairyCondition dairyCondition = dairyConditionRepository.findByDairyDairyIdAndDairyIsDeleteAndIsDeleteOrderByOrderNoAsc(CommonTestCode.DAIRY_ID1, BooleanYN.N.toString(), BooleanYN.N.toString())
                                                                    .orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(dairyCondition.getCondition().getCondId()).isEqualTo(CommonTestCode.COND_ID1);
    }

    @Test
    void existsByDairyDairyIdAndDairyIsDeleteAndIsDeleteN() {
        try {
            //given
            Dairy newDairy = commonTestCode.saveNewDairy();
        boolean result = dairyConditionRepository.existsByDairyDairyIdAndDairyIsDeleteAndIsDelete(newDairy.getDairyId(), BooleanYN.N, BooleanYN.N);
            boolean result = dairyConditionRepository.existsByDairyDairyIdAndDairyIsDeleteAndIsDelete(newDairy.getDairyId(), BooleanYN.N.toString(), BooleanYN.N.toString());
            //then
            Assertions.assertThat(result).isFalse();
        } catch (IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void existsByDairyDairyIdAndDairyIsDeleteAndIsDeleteY() {
        try {
            //given
            Dairy newDairy = commonTestCode.saveNewDairyCondition();
        boolean result = dairyConditionRepository.existsByDairyDairyIdAndDairyIsDeleteAndIsDelete(newDairy.getDairyId(), BooleanYN.N, BooleanYN.N);
            boolean result = dairyConditionRepository.existsByDairyDairyIdAndDairyIsDeleteAndIsDelete(newDairy.getDairyId(), BooleanYN.N.toString(), BooleanYN.N.toString());
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
        Integer cnt = dairyConditionRepository.getAllDairyConditionCntInMonth(CommonTestCode.MEMBER_ID, monthParam).orElseThrow(NullPointerException::new);
        //when
        ConditionRatio result = dairyConditionRepository.findConditionRatioListInMonth(CommonTestCode.MEMBER_ID, monthParam,1, cnt).orElseThrow(NullPointerException::new).get(0);
        //then
        Assertions.assertThat(result.getCondId()).isEqualTo(CommonTestCode.COND_ID1);
    }

    @Test
    void getAllDairyConditionCntInMonth() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        //when
        Integer result = dairyConditionRepository.getAllDairyConditionCntInMonth(CommonTestCode.MEMBER_ID, monthParam).orElseThrow(NullPointerException::new);
        //then
        Assertions.assertThat(result).isEqualTo(20);
    }
}
