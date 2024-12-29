package com.soothee.stats.service;

import com.soothee.common.constants.ContentType;
import com.soothee.common.constants.SortType;
import com.soothee.config.TestConfig;
import com.soothee.custom.exception.*;
import com.soothee.stats.controller.response.*;
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
    void getMonthlyDairyStats() {
        try {
            //given
            //when
            MonthlyDairyStats result = statsService.getMonthlyDairyStats(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.MONTH);
            //then
            Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
            Assertions.assertThat(result.getMostCondId()).isEqualTo(CommonTestCode.COND_ID1);
        } catch (DuplicatedResultException | ErrorToSearchStatsException |
                 NotFoundDetailInfoException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getMonthlyContentStatsT() {
        try {
            //given
            //when
            MonthlyContentStats result = statsService.getMonthlyContentStats(CommonTestCode.MEMBER_ID, ContentType.THANKS, CommonTestCode.YEAR, CommonTestCode.MONTH);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getHighest().getContent()).isEqualTo("땡큐");
            Assertions.assertThat(result.getLowest().getContent()).isEqualTo("감사");
        } catch (DuplicatedResultException | NotFoundDetailInfoException |
                 ErrorToSearchStatsException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getMonthlyContentStatsL() {
        try {
            //given
            //when
            MonthlyContentStats result = statsService.getMonthlyContentStats(CommonTestCode.MEMBER_ID, ContentType.LEARN, CommonTestCode.YEAR, CommonTestCode.MONTH);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getHighest().getContent()).isEqualTo("배운");
            Assertions.assertThat(result.getLowest().getContent()).isEqualTo("공부");
        } catch (DuplicatedResultException | NotFoundDetailInfoException |
                 ErrorToSearchStatsException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getWeeklyDairyStats() {
        try {
            //given
            //when
            WeeklyDairyStats result = statsService.getWeeklyDairyStats(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.WEEK);
            //then
            Assertions.assertThat(result.getScoreAvg()).isEqualTo(4.5);
            Assertions.assertThat(result.getCount()).isEqualTo(5);
            for (DateScore score : result.getScoreList()) {
                if (Objects.equals(score.getDate(), CommonTestCode.DATE1)) {
                    Assertions.assertThat(score.getScore()).isEqualTo(2.0);
                }
            }
        } catch (DuplicatedResultException | ErrorToSearchStatsException |
                 NotFoundDetailInfoException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getMonthlyConditionStats() {
        try {
            //given
            //when
            MonthlyConditionsStats result = statsService.getMonthlyConditionStats(CommonTestCode.MEMBER_ID, CommonTestCode.YEAR, CommonTestCode.MONTH);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(20);
            Assertions.assertThat(result.getCondiList().get(0).getCondId()).isEqualTo(CommonTestCode.COND_ID1);
            Assertions.assertThat(result.getCondiList().get(0).getCondRatio()).isEqualTo(25.0);
            Assertions.assertThat(result.getCondiList().get(1).getCondId()).isEqualTo(CommonTestCode.COND_ID3);
            Assertions.assertThat(result.getCondiList().get(1).getCondRatio()).isEqualTo(25.0);
            Assertions.assertThat(result.getCondiList().get(2).getCondId()).isEqualTo(CommonTestCode.COND_ID2);
            Assertions.assertThat(result.getCondiList().get(2).getCondRatio()).isEqualTo(25.0);
        } catch (NotFoundDetailInfoException | ErrorToSearchStatsException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getMonthlyContentDetailThanksDate() {
        try {
            //given
            //when
            MonthlyContentDetail result = statsService.getMonthlyContentDetail(CommonTestCode.MEMBER_ID, ContentType.THANKS, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.DATE);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("oh");
        } catch (ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getMonthlyContentDetailThanksHigh() {
        try {
            //given
            //when
            MonthlyContentDetail result = statsService.getMonthlyContentDetail(CommonTestCode.MEMBER_ID, ContentType.THANKS, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.HIGH);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("땡큐");
        } catch (ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getMonthlyContentDetailThanksLow() {
        try {
            //given
            //when
            MonthlyContentDetail result = statsService.getMonthlyContentDetail(CommonTestCode.MEMBER_ID, ContentType.THANKS, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.LOW);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("감사");
        } catch (ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getMonthlyContentDetailLearnDate() {
        try {
            //given
            //when
            MonthlyContentDetail result = statsService.getMonthlyContentDetail(CommonTestCode.MEMBER_ID, ContentType.LEARN, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.DATE);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("no");
        } catch (ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getMonthlyContentDetailLearnHigh() {
        try {
            //given
            //when
            MonthlyContentDetail result = statsService.getMonthlyContentDetail(CommonTestCode.MEMBER_ID, ContentType.LEARN, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.HIGH);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("배운");
        } catch (ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error("\n", e);
        }
    }

    @Test
    void getMonthlyContentDetailLearnLow() {
        try {
            //given
            //when
            MonthlyContentDetail result = statsService.getMonthlyContentDetail(CommonTestCode.MEMBER_ID, ContentType.LEARN, CommonTestCode.YEAR, CommonTestCode.MONTH, SortType.LOW);
            //then
            Assertions.assertThat(result.getCount()).isEqualTo(3);
            Assertions.assertThat(result.getContentList().get(0).getContent()).isEqualTo("공부");
        } catch (ErrorToSearchStatsException | NotFoundDetailInfoException e) {
            log.error("\n", e);
        }
    }
}