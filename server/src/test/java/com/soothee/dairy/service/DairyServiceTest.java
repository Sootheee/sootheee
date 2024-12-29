package com.soothee.dairy.service;

import com.soothee.common.constants.BooleanYN;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.config.TestConfig;
import com.soothee.custom.exception.*;
import com.soothee.dairy.domain.Dairy;
import com.soothee.dairy.dto.DairyDTO;
import com.soothee.dairy.dto.DairyRegisterDTO;
import com.soothee.dairy.dto.DairyScoresDTO;
import com.soothee.util.CommonTestCode;
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
class DairyServiceTest {
    @Autowired
    private DairyService dairyService;
    @Autowired
    private CommonTestCode commonTestCode;

    @Test
    void getAllDairyMonthly() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            List<DairyScoresDTO> result = dairyService.getAllDairyMonthly(CommonTestCode.MEMBER_ID, monthParam);
            //then
            Assertions.assertThat(result.size()).isEqualTo(5);
            Assertions.assertThat(result.get(2).getScore()).isEqualTo(1.0);
        } catch (IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getDairyByDate() {
        try {
            //given
            //when
            DairyDTO result = dairyService.getDairyByDate(CommonTestCode.MEMBER_ID, CommonTestCode.DATE4);
            //then
            Assertions.assertThat(result.getScore()).isEqualTo(9.5);
            Assertions.assertThat(result.getCondIdList().get(2)).isEqualTo(CommonTestCode.COND_ID2);
        } catch (DuplicatedResultException | NotExistDairyException | NotFoundDetailInfoException |
                 IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getDairyByDairyId() {
        try {
            //given
            //when
            DairyDTO result = dairyService.getDairyByDairyId(CommonTestCode.MEMBER_ID, CommonTestCode.DAIRY_ID2);
            //then
            Assertions.assertThat(result.getScore()).isEqualTo(5.5);
            Assertions.assertThat(result.getCondIdList().get(2)).isEqualTo(CommonTestCode.COND_ID3);
        } catch (DuplicatedResultException | NotExistDairyException | NotFoundDetailInfoException |
                 IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void registerDairy() {
        try {
            //given
            Dairy newDairy = commonTestCode.getNewDairy();
            DairyRegisterDTO inputInfo = DairyRegisterDTO.builder()
                                                        .date(newDairy.getDate())
                                                        .weatherId(newDairy.getWeather().getWeatherId())
                                                        .score(newDairy.getScore())
                                                        .condIdList(null)
                                                        .content(null)
                                                        .hope(null)
                                                        .thank(null)
                                                        .learn(null)
                                                        .build();
            //when
            dairyService.registerDairy(CommonTestCode.MEMBER_ID, inputInfo);
            //then
            Dairy result = commonTestCode.getSavedNewDairy(newDairy.getDate());
            Assertions.assertThat(result.getScore()).isEqualTo(3.0);
        } catch (IncorrectValueException | DuplicatedResultException | NotExistMemberException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void modifyDairyWCondIds() {
        try {
            //given
            DairyDTO modifyInfo = DairyDTO.builder()
                    .dairyId(CommonTestCode.DAIRY_ID3)
                    .score(1.0)
                    .date(CommonTestCode.DATE3)
                    .weatherId(CommonTestCode.WEATHER_ID)
                    .content("contents")
                    .build();
            List<String> modifyCondIds = new ArrayList<>();
            modifyCondIds.add(CommonTestCode.NEW_COND_ID1);
            modifyCondIds.add(CommonTestCode.NEW_COND_ID2);
            modifyCondIds.add(CommonTestCode.NEW_COND_ID3);
            modifyInfo.setCondIdList(modifyCondIds);
            //when
            dairyService.modifyDairy(CommonTestCode.MEMBER_ID, CommonTestCode.DAIRY_ID3, modifyInfo);
            //then
            Dairy savedDairy = commonTestCode.getSavedDairy(CommonTestCode.DAIRY_ID3);
            Assertions.assertThat(savedDairy.getContent()).isEqualTo("contents");
        } catch (IncorrectValueException | NotMatchedException | NullValueException | NotExistDairyException |
                 NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void modifyDairyWoCondIds() {
        try {
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
        } catch (IncorrectValueException | NotMatchedException | NullValueException | NotExistDairyException |
                 NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void deleteDairy() {
        try {
            //given
            Dairy newDairy = commonTestCode.saveNewDairy();
            //when
            dairyService.deleteDairy(CommonTestCode.MEMBER_ID, newDairy.getDairyId());
            //then
            Assertions.assertThat(newDairy.getIsDelete()).isEqualTo(BooleanYN.Y);
        } catch (IncorrectValueException | NotMatchedException | NullValueException | NotExistDairyException |
                 NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }
}