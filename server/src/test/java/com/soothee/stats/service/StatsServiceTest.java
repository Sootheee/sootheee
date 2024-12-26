package com.soothee.stats.service;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.common.requestParam.MonthParam;
import com.soothee.common.requestParam.WeekParam;
import com.soothee.config.TestConfig;
import com.soothee.custom.exception.*;
import com.soothee.stats.dto.*;
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

import java.util.Objects;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Transactional
@EnableJpaAuditing
@ActiveProfiles("test")
@Slf4j
@Import(TestConfig.class)
class StatsServiceTest {
    @Autowired
    private StatsService statsService;

    @Test
    void getMonthlyStatsInfo() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyStatsDTO result = statsService.getMonthlyStatsInfo(CommonTestCode.MEMBER_ID, monthParam);
            //then
            Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
            Assertions.assertThat(result.getMostCondId()).isEqualTo(CommonTestCode.COND_ID1);
        } catch (DuplicatedResultException |
                 IncorrectValueException | NullValueException | ErrorToSearchStatsException |
                 NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getMonthlyContentsT() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyContentsDTO result = statsService.getMonthlyContents(CommonTestCode.MEMBER_ID, ContentType.THANKS, monthParam);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getHighest().getContent()).isEqualTo("땡큐");
            Assertions.assertThat(result.getLowest().getContent()).isEqualTo("감사");
        } catch (DuplicatedResultException | IncorrectValueException |
                 NullValueException | NotFoundDetailInfoException |
                 ErrorToSearchStatsException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getMonthlyContentsL() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyContentsDTO result = statsService.getMonthlyContents(CommonTestCode.MEMBER_ID, ContentType.LEARN, monthParam);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getHighest().getContent()).isEqualTo("배운");
            Assertions.assertThat(result.getLowest().getContent()).isEqualTo("공부");
        } catch (DuplicatedResultException | IncorrectValueException |
                 NullValueException | NotFoundDetailInfoException |
                 ErrorToSearchStatsException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getWeeklyStatsInfo() {
        //given
        WeekParam weekParam = new WeekParam(CommonTestCode.YEAR, CommonTestCode.WEEK);
        try {
            //when
            WeeklyStatsDTO result = statsService.getWeeklyStatsInfo(CommonTestCode.MEMBER_ID, weekParam);
            //then
            Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
            Assertions.assertThat(result.getCount()).isEqualTo(5);
            for (DateScore score : result.getScoreList()) {
                if (Objects.equals(score.getDate(), CommonTestCode.DATE1)) {
                    Assertions.assertThat(score.getScore()).isEqualTo(2.0);
                }
            }
        } catch (DuplicatedResultException | ErrorToSearchStatsException |
                 NotFoundDetailInfoException | IncorrectValueException | NullValueException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getMonthlyConditionList() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyConditionsDTO result = statsService.getMonthlyConditionList(CommonTestCode.MEMBER_ID, monthParam);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(20);
            Assertions.assertThat(result.getCondiList().get(0).getCondId()).isEqualTo(CommonTestCode.COND_ID1);
            Assertions.assertThat(result.getCondiList().get(0).getCondRatio()).isEqualTo(25.0);
            Assertions.assertThat(result.getCondiList().get(1).getCondId()).isEqualTo(CommonTestCode.COND_ID3);
            Assertions.assertThat(result.getCondiList().get(1).getCondRatio()).isEqualTo(25.0);
            Assertions.assertThat(result.getCondiList().get(2).getCondId()).isEqualTo(CommonTestCode.COND_ID2);
            Assertions.assertThat(result.getCondiList().get(2).getCondRatio()).isEqualTo(25.0);
        } catch (IncorrectValueException | NullValueException |
                 NotFoundDetailInfoException | ErrorToSearchStatsException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getAllContentsInMonthThanksDate() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS, monthParam, SortType.DATE);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("oh");
        } catch (IncorrectValueException | NullValueException | ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getAllContentsInMonthThanksHigh() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS, monthParam, SortType.HIGH);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("땡큐");
        } catch (IncorrectValueException | NullValueException | ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getAllContentsInMonthThanksLow() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.THANKS, monthParam, SortType.LOW);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("감사");
        } catch (IncorrectValueException | NullValueException | ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getAllContentsInMonthLearnDate() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.LEARN, monthParam, SortType.DATE);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("no");
        } catch (IncorrectValueException | NullValueException | ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getAllContentsInMonthLearnHigh() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.LEARN, monthParam, SortType.HIGH);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("배운");
        } catch (IncorrectValueException | NullValueException | ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    void getAllContentsInMonthLearnLow() {
        //given
        MonthParam monthParam = new MonthParam(CommonTestCode.YEAR, CommonTestCode.MONTH);
        try {
            //when
            MonthlyAllContentsDTO result = statsService.getAllContentsInMonth(CommonTestCode.MEMBER_ID, ContentType.LEARN, monthParam, SortType.LOW);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("공부");
        } catch (IncorrectValueException | NullValueException | ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error(e.getMessage());
        }
    }
}