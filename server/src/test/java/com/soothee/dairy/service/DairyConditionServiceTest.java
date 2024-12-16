package com.soothee.dairy.service;

import com.soothee.config.TestConfig;
import com.soothee.util.CommonTestCode;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.domain.DairyCondition;
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
@Import(TestConfig.class)
class DairyConditionServiceTest {
    @Autowired
    private DairyConditionServiceImpl dairyConditionService;
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void getConditionsIdListByDairy() {
        //given
        commonTestCode.saveNewDairyCondition();
        //when
        List<Long> dairyConditionList = dairyConditionService.getConditionsIdListByDairy(CommonTestCode.DAIRY_ID1);
        //then
        Assertions.assertThat(dairyConditionList.get(0)).isEqualTo(1L);
        Assertions.assertThat(dairyConditionList.get(1)).isEqualTo(7L);
        Assertions.assertThat(dairyConditionList.get(2)).isEqualTo(2L);
    }

    @Test
    void saveConditions() {
        //given
        Dairy savedNewDairy = commonTestCode.saveNewDairy();
        List<Long> condIds = new ArrayList<>();
        condIds.add(1L);
        condIds.add(2L);
        condIds.add(3L);
        //when
        dairyConditionService.saveConditions(condIds, savedNewDairy);
        //then
        List<DairyCondition> afterDcList = commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(),"after");
        Assertions.assertThat(afterDcList.size()).isEqualTo(3);
    }

    @Test
    void updateConditions() {
        //given
        Dairy savedNewDairy = commonTestCode.saveNewDairyCondition();
        commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "before");
        //when
        List<Long> newCondIds = new ArrayList<>();
        newCondIds.add(4L);
        newCondIds.add(2L);
        newCondIds.add(1L);
        dairyConditionService.updateConditions(savedNewDairy, newCondIds);
        //then
        List<DairyCondition> afterDcList = commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "after");
        Assertions.assertThat(afterDcList.size()).isEqualTo(3);
        Assertions.assertThat(afterDcList.get(0).getCondition().getCondId()).isEqualTo(4L);
        Assertions.assertThat(afterDcList.get(1).getCondition().getCondId()).isEqualTo(2L);
        Assertions.assertThat(afterDcList.get(2).getCondition().getCondId()).isEqualTo(1L);
    }

    @Test
    void updateConditionsBiggerInput() {
        //given
        //when
        Dairy savedNewDairy = commonTestCode.saveNewDairyCondition();
        commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "before");
        List<Long> newCondIds = new ArrayList<>();
        newCondIds.add(4L);
        newCondIds.add(2L);
        newCondIds.add(1L);
        newCondIds.add(5L);
        newCondIds.add(6L);
        dairyConditionService.updateConditions(savedNewDairy, newCondIds);
        //then
        List<DairyCondition> afterDcList = commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "after");
        Assertions.assertThat(afterDcList.size()).isEqualTo(5);
        Assertions.assertThat(afterDcList.get(0).getCondition().getCondId()).isEqualTo(4L);
        Assertions.assertThat(afterDcList.get(1).getCondition().getCondId()).isEqualTo(2L);
        Assertions.assertThat(afterDcList.get(2).getCondition().getCondId()).isEqualTo(1L);
        Assertions.assertThat(afterDcList.get(3).getCondition().getCondId()).isEqualTo(5L);
        Assertions.assertThat(afterDcList.get(4).getCondition().getCondId()).isEqualTo(6L);
    }

    @Test
    void updateConditionsBiggerCur() {
        //given
        //when
        Dairy savedNewDairy = commonTestCode.saveNewDairyCondition();
        commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "before");
        List<Long> newCondIds = new ArrayList<>();
        newCondIds.add(4L);
        newCondIds.add(2L);
        newCondIds.add(1L);
        dairyConditionService.updateConditions(savedNewDairy, newCondIds);
        //then
        List<DairyCondition> afterDcList = commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "after");
        Assertions.assertThat(afterDcList.size()).isEqualTo(3);
        Assertions.assertThat(afterDcList.get(0).getCondition().getCondId()).isEqualTo(4L);
        Assertions.assertThat(afterDcList.get(1).getCondition().getCondId()).isEqualTo(2L);
        Assertions.assertThat(afterDcList.get(2).getCondition().getCondId()).isEqualTo(1L);
    }

    @Test
    void deleteDairyConditionsOfDairy() {
        //given
        Dairy savedNewDairy = commonTestCode.saveNewDairyCondition();
        //when
        dairyConditionService.deleteDairyConditionsOfDairy(savedNewDairy);
        //then
        int cnt = commonTestCode.getNewDairyConditions(savedNewDairy.getDairyId(), "after").size();
        Assertions.assertThat(cnt).isEqualTo(0);
    }
}