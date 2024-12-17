package com.soothee.dairy.service;

import com.soothee.config.TestConfig;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyRegisterDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.util.CommonTestCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class DairyServiceTest {
    @Autowired
    private DairyService dairyService;
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void getAllDairyMonthly() {
        //given
        //when
        List<DairyScoresDTO> result = dairyService.getAllDairyMonthly(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.MONTH);
        //then
        Assertions.assertThat(result.size()).isEqualTo(5);
        Assertions.assertThat(result.get(2).getScore()).isEqualTo(1.0);
    }

    @Test
    void getDairyByDate() {
        //given
        //when
        DairyDTO result = dairyService.getDairyByDate(CommonTestCode.MEMBER_ID, CommonTestCode.DATE4);
        //then
        Assertions.assertThat(result.getScore()).isEqualTo(9.5);
        Assertions.assertThat(result.getCondIds().get(2)).isEqualTo(7L);
    }

    @Test
    void getDairyByDairyId() {
        //given
        //when
        DairyDTO result = dairyService.getDairyByDairyId(CommonTestCode.MEMBER_ID, CommonTestCode.DAIRY_ID2);
        //then
        Assertions.assertThat(result.getScore()).isEqualTo(5.5);
        Assertions.assertThat(result.getCondIds().get(2)).isEqualTo(2L);
    }

    @Test
    void registerDairy() {
        //given
        Dairy newDairy = commonTestCode.getNewDairy();
        DairyRegisterDTO inputInfo = new DairyRegisterDTO(newDairy.getDate(), newDairy.getWeather().getWeatherId(), newDairy.getScore(), null, null, null, null, null);
        //when
        dairyService.registerDairy(CommonTestCode.MEMBER_ID, inputInfo);
        //then
        Dairy result = commonTestCode.getSavedNewDairy(newDairy.getDate());
        Assertions.assertThat(result.getScore()).isEqualTo(3.0);
    }

    @Test
    void modifyDairyWCondIds() {
        //given
        DairyDTO modifyInfo = DairyDTO.builder()
                .dairyId(CommonTestCode.DAIRY_ID3)
                .score(1.0)
                .date(CommonTestCode.DATE3)
                .weatherId(CommonTestCode.WEATHER_ID)
                .content("contents")
                .build();
        List<Long> modifyCondIds = new ArrayList<>();
        modifyCondIds.add(CommonTestCode.NEW_COND_ID1);
        modifyCondIds.add(CommonTestCode.NEW_COND_ID2);
        modifyCondIds.add(CommonTestCode.NEW_COND_ID3);
        modifyInfo.setCondIds(modifyCondIds);
        //when
        dairyService.modifyDairy(CommonTestCode.MEMBER_ID, CommonTestCode.DAIRY_ID3, modifyInfo);
        //then
        Dairy savedDairy = commonTestCode.getSavedDairy(CommonTestCode.DAIRY_ID3);
        Assertions.assertThat(savedDairy.getContent()).isEqualTo("contents");
    }

    @Test
    void modifyDairyWoCondIds() {
        //given
        DairyDTO modifyInfo = DairyDTO.builder()
                .dairyId(CommonTestCode.DAIRY_ID3)
                .score(1.0)
                .date(CommonTestCode.DATE3)
                .weatherId(CommonTestCode.WEATHER_ID)
                .content("contents")
                .build();
        //when
        dairyService.modifyDairy(CommonTestCode.MEMBER_ID, CommonTestCode.DAIRY_ID3, modifyInfo);
        //then
        Dairy savedDairy = commonTestCode.getSavedDairy(CommonTestCode.DAIRY_ID3);
        Assertions.assertThat(savedDairy.getContent()).isEqualTo("contents");
    }

    @Test
    void deleteDairy() {
        //given
        Dairy newDairy = commonTestCode.saveNewDairy();
        //when
        dairyService.deleteDairy(CommonTestCode.MEMBER_ID, newDairy.getDairyId());
        //then
        Assertions.assertThat(newDairy.getIsDelete()).isEqualTo("Y");
    }
}