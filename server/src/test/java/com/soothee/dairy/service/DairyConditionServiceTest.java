package com.soothee.dairy.service;

import com.soothee.config.TestConfig;
import com.soothee.custom.exception.*;
import com.soothee.util.CommonTestCode;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ArrayList;
import java.util.List;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
@Slf4j
@Import(TestConfig.class)
class DairyConditionServiceTest {
    @Autowired
    private DairyConditionServiceImpl dairyConditionService;
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void saveConditions() {
        try {
            //given
            Dairy savedNewDairy = commonTestCode.saveNewDairy();
            List<String> condIds = new ArrayList<>();
            condIds.add("COND001");
            condIds.add("COND002");
            condIds.add("COND003");
            //when
            dairyConditionService.saveConditions(condIds, savedNewDairy);
            //then
            List<DairyCondition> afterDcList = commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "after");
            Assertions.assertThat(afterDcList.size()).isEqualTo(3);
        } catch (IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getConditionsIdListByDairy() {
        try {
            //given
            //when
            List<String> conditionIdList = dairyConditionService.getConditionsIdListByDairy(CommonTestCode.DAIRY_ID1);
            //then
            Assertions.assertThat(conditionIdList.get(0)).isEqualTo(CommonTestCode.COND_ID1);
            Assertions.assertThat(conditionIdList.get(1)).isEqualTo(CommonTestCode.COND_ID2);
            Assertions.assertThat(conditionIdList.get(2)).isEqualTo(CommonTestCode.COND_ID3);
        } catch (NotFoundDetailInfoException | IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void updateConditions() {
        try {
            //given
            Dairy savedNewDairy = commonTestCode.saveNewDairyCondition();
            commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "before");
            //when
            List<String> newCondIds = new ArrayList<>();
            newCondIds.add("COND004");
            newCondIds.add("COND002");
            newCondIds.add("COND001");
            dairyConditionService.updateConditions(savedNewDairy, newCondIds);
            //then
            List<DairyCondition> afterDcList = commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "after");
            Assertions.assertThat(afterDcList.size()).isEqualTo(3);
            Assertions.assertThat(afterDcList.get(0).getCondition().getCondId()).isEqualTo("COND004");
            Assertions.assertThat(afterDcList.get(1).getCondition().getCondId()).isEqualTo("COND002");
            Assertions.assertThat(afterDcList.get(2).getCondition().getCondId()).isEqualTo("COND001");
        } catch (NullValueException | NotMatchedException | IncorrectValueException |
                 NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void updateConditionsBiggerInput() {
        try {
            //given
            //when
            Dairy savedNewDairy = commonTestCode.saveNewDairyCondition();
            commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "before");
            List<String> newCondIds = new ArrayList<>();
            newCondIds.add("COND004");
            newCondIds.add("COND002");
            newCondIds.add("COND001");
            newCondIds.add("COND005");
            newCondIds.add("COND006");
            dairyConditionService.updateConditions(savedNewDairy, newCondIds);
            //then
            List<DairyCondition> afterDcList = commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "after");
            Assertions.assertThat(afterDcList.size()).isEqualTo(5);
            Assertions.assertThat(afterDcList.get(0).getCondition().getCondId()).isEqualTo("COND004");
            Assertions.assertThat(afterDcList.get(1).getCondition().getCondId()).isEqualTo("COND002");
            Assertions.assertThat(afterDcList.get(2).getCondition().getCondId()).isEqualTo("COND001");
            Assertions.assertThat(afterDcList.get(3).getCondition().getCondId()).isEqualTo("COND005");
            Assertions.assertThat(afterDcList.get(4).getCondition().getCondId()).isEqualTo("COND006");
        } catch (NullValueException | NotMatchedException | IncorrectValueException |
                 NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void updateConditionsBiggerCur() {
        try {
            //given
            //when
            Dairy savedNewDairy = commonTestCode.saveNewDairyCondition();
            commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "before");
            List<String> newCondIds = new ArrayList<>();
            newCondIds.add("COND004");
            newCondIds.add("COND002");
            newCondIds.add("COND001");
            dairyConditionService.updateConditions(savedNewDairy, newCondIds);
            //then
            List<DairyCondition> afterDcList = commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "after");
            Assertions.assertThat(afterDcList.size()).isEqualTo(3);
            Assertions.assertThat(afterDcList.get(0).getCondition().getCondId()).isEqualTo("COND004");
            Assertions.assertThat(afterDcList.get(1).getCondition().getCondId()).isEqualTo("COND002");
            Assertions.assertThat(afterDcList.get(2).getCondition().getCondId()).isEqualTo("COND001");
        } catch (NullValueException | NotMatchedException | IncorrectValueException |
                 NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void deleteDairyConditionsOfDairy() {
        try {
            //given
            Dairy savedNewDairy = commonTestCode.saveNewDairyCondition();
            //when
            dairyConditionService.deleteDairyConditionsOfDairy(savedNewDairy);
            //then
            int cnt = commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "after").size();
            Assertions.assertThat(cnt).isEqualTo(0);
        } catch (IncorrectValueException | NullValueException | NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }
}